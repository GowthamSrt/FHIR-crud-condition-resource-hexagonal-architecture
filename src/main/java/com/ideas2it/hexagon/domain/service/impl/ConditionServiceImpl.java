package com.ideas2it.hexagon.domain.service.impl;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.ideas2it.hexagon.application.dto.ConditionDto;
import com.ideas2it.hexagon.config.FhirConfig;
import com.ideas2it.hexagon.domain.exception.ResourceNotFoundException;
import com.ideas2it.hexagon.domain.service.ConditionService;
import com.ideas2it.hexagon.infrastructure.mapper.ConditionMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hl7.fhir.r4.model.Condition;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConditionServiceImpl implements ConditionService {

    private static final Logger LOGGER = LogManager.getLogger(ConditionServiceImpl.class);
    private static final IGenericClient client = FhirConfig.getClient();
    private final ConditionMapper conditionMapper;
    private final ObjectMapper objectMapper;

    @Override
    public ConditionDto addCondition(ConditionDto conditionDto) {
        val condition = conditionMapper.toEntity(conditionDto);
        val addedCondition = (Condition) client.create()
                .resource(condition)
                .execute()
                .getResource();
        LOGGER.info("Condition added successfully!");
        return conditionMapper.toDto(addedCondition);
    }

    @Override
    public ConditionDto getConditionById(Long id) {
        Condition condition = Optional.ofNullable((Condition) client.read()
                .resource(Condition.class)
                .withId(id)
                .execute())
                .orElseThrow(() -> {
                    LOGGER.warn("Condition with Given Id : " + id + " is not found!");
                    throw new ResourceNotFoundException("Condition with Given Id : " + id + " is not found!");
                });
        LOGGER.info("Retrieved Condition with Id : " + id);
        return conditionMapper.toDto(condition);
    }

    @Override
    public String removeCondition(Long id) {
        Condition condition = Optional.ofNullable((Condition) client.read()
                        .resource(Condition.class)
                        .withId(id)
                        .execute())
                .orElseThrow(() -> {
                    LOGGER.warn("Condition with Given Id : " + id + " is not found!");
                    throw new ResourceNotFoundException("Condition with Given Id : " + id + " is not found!");
                });
        client.delete()
                .resource(condition)
                .execute();
        LOGGER.info("Condition removed successfully with Id : " + id);
        return "Deleted Successfully!";
    }

    @Override
    public ConditionDto updateCondition(Long id, JsonPatch patch) {
        Condition condition = Optional.ofNullable((Condition) client.read()
                        .resource(Condition.class)
                        .withId(id)
                        .execute())
                .orElseThrow(() -> {
                    LOGGER.error("Condition with ID " + id + " not found for update.");
                    return new ResourceNotFoundException("Condition with ID " + id + " not found for update.");
                });
        ConditionDto conditionDto = conditionMapper.toDto(condition);
        ConditionDto patchedDto = applyPatch(patch, conditionDto);
        Condition patchedCondition = conditionMapper.toEntity(patchedDto);
        patchedCondition.setId(id.toString());
        Condition updatedCondition = (Condition) client.update()
                .resource(patchedCondition)
                .execute()
                .getResource();
        LOGGER.info("Condition updated successfully with ID: " + id);
        return conditionMapper.toDto(updatedCondition);
    }

    private ConditionDto applyPatch(JsonPatch patch, ConditionDto conditionDto) {
        try {
            JsonNode conditionNode = objectMapper.convertValue(conditionDto, JsonNode.class);
            JsonNode patchedNode = patch.apply(conditionNode);
            return objectMapper.treeToValue(patchedNode, ConditionDto.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException("Failed to apply JSON patch to condition dto!");
        }
    }
}
