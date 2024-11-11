package com.ideas2it.hexagon.infrastructure.mapper;

import com.ideas2it.hexagon.application.dto.ConditionDto;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.UUID;

@Component
public class ConditionMapper {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public Condition toEntity(ConditionDto conditionDto) {
        Condition condition = new Condition();
        condition.addIdentifier()
                .setSystem("http://acme.org/mrns")
                .setValue(UUID.randomUUID().toString());
        condition.setClinicalStatus(new CodeableConcept().addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical")
                .setCode(conditionDto.getClinicalStatus())));
        condition.setVerificationStatus(new CodeableConcept().addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/condition-ver-status")
                .setCode(conditionDto.getVerificationStatus())));
        condition.setCode(new CodeableConcept().addCoding(new Coding()
                .setSystem("http://snomed.info/sct")
                .setCode(conditionDto.getCode())
                .setDisplay(conditionDto.getCode())));
        condition.setRecordedDate(conditionDto.getRecordedDate());
        return condition;
    }

    public ConditionDto toDto(Condition condition) {
        ConditionDto conditionDto = new ConditionDto();
        conditionDto.setId(condition.getIdElement().getIdPart());
        Optional.ofNullable(condition.getClinicalStatus())
                .map(CodeableConcept::getCodingFirstRep)
                .map(Coding::getCode)
                .ifPresent(conditionDto::setClinicalStatus);
        Optional.ofNullable(condition.getVerificationStatus())
                .map(CodeableConcept::getCodingFirstRep)
                .map(Coding::getCode)
                .ifPresent(conditionDto::setVerificationStatus);
        Optional.ofNullable(condition.getCode())
                .map(CodeableConcept::getCodingFirstRep)
                .map(Coding::getCode)
                .ifPresent(conditionDto::setCode);
        if (condition.hasRecordedDate()) {
            conditionDto.setRecordedDate(condition.getRecordedDate());
        }
        return conditionDto;
    }
}
