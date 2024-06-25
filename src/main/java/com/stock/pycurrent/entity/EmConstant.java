package com.stock.pycurrent.entity;

import com.stock.pycurrent.entity.jsonvalue.EmConstantValue;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "em_constants")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmConstant {
    @Column(name = "c_key", length = 32)
    @Id
    private String cKey;

    @Column(name = "c_value", length = 256)
    private String cValue;

    @Column(columnDefinition = "jsonb", name = "multi_value")
    @Type(JsonType.class)
    private List<EmConstantValue> multiValue;
    @Transient
    private String multiValueStr;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmConstant that = (EmConstant) o;
        return Objects.equals(cKey, that.cKey) && Objects.equals(cValue, that.cValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cKey, cValue);
    }
}