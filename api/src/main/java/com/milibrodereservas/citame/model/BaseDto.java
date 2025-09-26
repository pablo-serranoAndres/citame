package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.UserBusiness;
import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.global.RepositoryRegistry;
import com.milibrodereservas.citame.global.SpringContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Clase base para todos los objetos DTO
// Los objetos DTO son una copia de los objetos entidad que el servicio devuelve al MB
// El controlador MB no debería usar el objeto entidad (no sé por qué)
// El objeto DTO va a tener solo los campos que necesita el MB (al menos para datos muy complejos con tablas relacionadas)

public abstract class BaseDto extends Base {
	protected BaseDto() {
		super();
    }
	
	// Usamos reflexion en este metodo para setear las variables de this objeto
	// con los valores de las propiedades de entidad del mismo nombre
	// usando los metodos getter
	public void loadFromObject(Object entidad) {
		Class<?> targetClass = this.getClass();
		// Iterar sobre los getter del objeto entidad
		for (Method getMethod : entidad.getClass().getMethods()) {
			if (getMethod.getName().startsWith("get")) {
				// Obtener el nombre del campo fuente
				// Quitamos el get del principio del nombre y pasamos la siguiente letra a minuscula
				String fieldName = getMethod.getName();
				fieldName = fieldName.substring(3,4).toLowerCase()
						  + fieldName.substring(4);
				try {
					// Buscar un campo con el mismo nombre en este objeto
					Field targetField = targetClass.getDeclaredField(fieldName);
					if (getMethod.getReturnType() == targetField.getGenericType()) {
						// Obtener el valor del método getter del objeto entidad
						Object value = getMethod.invoke(entidad);
						// Los campos son del mismo tipo
						// evita que se metan automaticamente tipos derivados sin convertir a dto
						// Establecer la accesibilidad del campo para que podamos modificarlo
						targetField.setAccessible(true);
						// Establecer el valor
						targetField.set(this, value); 
						// Restaurar la accesibilidad del campo
						targetField.setAccessible(false);
					}
				} catch (NoSuchFieldException e) {
					// no hay un campo en este objeto con ese nombre, lo saltamos
				} catch (IllegalAccessException e) {
					// solo deberia suceder si un metodo get es no public
				} catch (IllegalArgumentException e) {
					// no deberia suceder nunca (pero si sucede nos olvidamos de este campo y continuamos)
					// no se si podria suceder si los tipos de las propiedades del mismo nombre fueran diferentes
				} catch (InvocationTargetException e) {
					// no deberia suceder nunca (idem anterior)
				}
		    }
		}
	}

	// store values in DTO from entity
	// excludedFields list of non stored fields (recursion for instance) pipe (|) separated
	public void storeInObject(Object entity) {
		storeInObject(entity, "");
	}
	public void storeInObject(Object entity, final String excludedFields) {
		final String excluded = "|" + excludedFields.toLowerCase() + "|";
		Class<?> entityClass = entity.getClass();
		Class<?> dtoClass = this.getClass();

		// Iterar sobre los setter del objeto entidad
		for (Method setter : entityClass.getMethods()) {
			if (setter.getName().startsWith("set") && setter.getParameterCount() == 1) {
				// Obtener el nombre del campo fuente
				String fieldName = setter.getName().substring(3);
				if (excluded.contains("|" + fieldName.toLowerCase() + "|")) {
					continue;
				}
				String idGetterName = "getId" + fieldName; // get del ID del campo
				String id2GetterName = "get" + fieldName + "Id";
				String normalGetterName = "get" + fieldName; // get de campo normal

				try {
					Method getter = null;
					Object dtoValue = null;

					if (hasMethod(dtoClass, idGetterName)) {
						getter = dtoClass.getMethod(idGetterName);
					}
					if (hasMethod(dtoClass, id2GetterName)) {
						getter = dtoClass.getMethod(id2GetterName);
					}
					if (getter != null) {// Si hay un getIdXxxx() o getXxxxId(), intentamos buscar la entidad relacionada con el DAO
						Object idValue = getter.invoke(this); // devuelve el ID de la entidad

						if (idValue != null) {
							Class<?> paramType = setter.getParameterTypes()[0]; // Tipo de la entidad relacionada
							// Registro (manual) de la clase dao para una entidad
							RepositoryRegistry repoRegistry = SpringContext.getApplicationContext().getBean(RepositoryRegistry.class);
							// repository para la entidad
							JpaRepository<?, ?> repo = repoRegistry.getRepoForEntity(paramType);
							if (repo != null) {
								try {
									// si idValue es el ID existira un findById para ese valor si no throws NoSuchMethodException
									Method findById = repo.getClass().getMethod("findById", idValue.getClass());
									Optional<?> result = (Optional<?>) findById.invoke(repo, idValue);
									Object relatedEntity = result.orElse(null);

									if (relatedEntity != null) {
										setter.invoke(entity, relatedEntity); // set de la entidad encontrada por ID
										continue; // saltamos el proceso normal
									}
								}catch (NoSuchMethodException ignore) { // idValue no es el ID de la entidad, continuamos normal
								}
							}
						}
					}

					// Si no hay getIdXxxx(), intentamos el getXxxx() normal (puede ser un DTO)
					getter = dtoClass.getMethod(normalGetterName);
					dtoValue = getter.invoke(this);

					if (dtoValue != null) {
						Class<?> paramType = setter.getParameterTypes()[0]; // tipo de dato esperado por el set de la entidad

						if (dtoValue instanceof BaseDto) { // se devuelve un DTO => paramType debe ser una entidad
							try {
								// busco el constructor de la entidad desde su dto
								Object relatedEntity = paramType.getConstructor(dtoValue.getClass()).newInstance(dtoValue);
								setter.invoke(entity, relatedEntity); // guardo la entidad relacionada
							} catch (NoSuchMethodException e) {
								logger.warn("No se encontró un constructor adecuado para " + paramType.getName());
							}
						} else { // valor normal, se guarda sin transformar
							setter.invoke(entity, dtoValue);
						}
					}
				} catch (NoSuchMethodException ignored) {
				} catch (Exception e) {
					logger.warn("Error al guardar campo {}", fieldName, e);
				}
			}
		}
	}

	// Verifica si un método existe en una clase
	private boolean hasMethod(Class<?> clazz, String methodName) {
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName)) {
				return true;
			}
		}
		return false;
	}

	public static <Entidad, DTO extends BaseDto> List<DTO> convertToListDto(List<Entidad> entidades, Class<DTO> dtoClass) {
		if (entidades != null) {
			List<DTO> listaRet = new ArrayList<>();
			if (!entidades.isEmpty()) {
				// Obtener el constructor Dto(Entidad)
				Constructor<DTO> constructor = null;
                try {
                    constructor = dtoClass.getConstructor(entidades.get(0).getClass());
                } catch (NoSuchMethodException e) {
                    Base.throwException("No se pudo obtener constructor con parámetro Entidad", e);
                }

                for (Entidad entidad : entidades) {
					DTO item = null;
					try {
						item = constructor.newInstance(entidad);
					} catch (Exception ex) {
						Base.throwException("No se pudo construir Dto", ex);
					}
					listaRet.add(item);
				}
			}
			return listaRet;
		}
		return null;
	}

	public static <Entidad, DTO extends BaseDto> List<Entidad> convertToListEntity(List<DTO> dtos, Class<Entidad> entidadClass) {
		if (dtos != null) {
			List<Entidad> listaRet = new ArrayList<>();
			if (!dtos.isEmpty()) {
				// Obtener el constructor Entidad(Dto)
				Constructor<Entidad> constructor = null;
				try {
					constructor = entidadClass.getConstructor(dtos.get(0).getClass());
				} catch (NoSuchMethodException e) {
					Base.throwException("No se pudo obtener constructor con parámetro Dto", e);
				}

				for (DTO dto : dtos) {
					Entidad item = null;
					try {
						item = constructor.newInstance(dto);
					} catch (Exception ex) {
						Base.throwException("No se pudo construir Entidad", ex);
					}
					listaRet.add(item);
				}
			}
			return listaRet;
		}
		return null;
	}
}