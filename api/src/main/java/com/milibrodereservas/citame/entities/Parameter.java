package com.milibrodereservas.citame.entities;

import com.milibrodereservas.citame.model.ParameterDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table (name = "parameters")
@TableGenerator(
		name = "id_parameter",
		table = "hibernate_sequences",
		pkColumnName = "sequence_name",
		valueColumnName = "next_val",
		pkColumnValue = "id_parameter",
		allocationSize = 1
)
public class Parameter implements Serializable {
	public final static int TYPEVALUE_CHECK = 0;
	public final static int TYPEVALUE_STRING = 1;  // auxValue = max length
	public final static int TYPEVALUE_INTEGER = 2;  // auxValue = range
	public final static int TYPEVALUE_FLOAT = 3;  // auxValue = range
	public final static int TYPEVALUE_LIST = 4;  // auxValue = list values
	public final static int TYPEVALUE_DATE = 5; // auxValue = format
	public final static int TYPEVALUE_TIME = 6; // auxValue = format
	public final static int TYPEVALUE_DATETIME = 7; // auxValue = format

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "id_parameter")
	private Long id;
	@ManyToOne
	@JoinColumn(name="idBusiness")
	private Business business; // Negocio al que pertenece el parámetro
	@Column(length=32, nullable=false)
	private String name; // Clave de parámetro (único por negocio)
	@Column(length=128, nullable=false)
	private String caption; // Texto en edición del parámetro
	@Column(nullable = false)
	private Integer typeValue; // Tipo de parámetro: 0=check, 1=string, 2=integer (auxValue=rango), 3=float (idem), 4=string desplegable (auxValue=valores separados por |)
	@Column(length=512)
	private String defaultValue; // Valor por defecto del parámetro
	@Column(length=512)
	private String auxValue; // Varios dependiendo de typeValue

	public Parameter(ParameterDto dto) {
		this();
		dto.storeInObject(this);
	}
}
