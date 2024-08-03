package org.project.document;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.project.helper.Indices;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.List;

@Getter
@Setter
@Document(indexName = Indices.PERSON_INDEX)
@Setting(settingPath = "static/es-setting.json")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
	@Id
	@Field(type = FieldType.Keyword)
	String id;
	@Field(type = FieldType.Text)
	String name;
	List<Vehicle> vehicleList;
}
