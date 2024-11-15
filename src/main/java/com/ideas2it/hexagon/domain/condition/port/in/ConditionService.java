package com.ideas2it.hexagon.domain.condition.port.in;

import com.github.fge.jsonpatch.JsonPatch;
import com.ideas2it.hexagon.domain.condition.dto.ConditionDto;

public interface ConditionService {
    ConditionDto addCondition(ConditionDto conditionDto);
    ConditionDto getConditionById(Long id);
    String removeCondition(Long id);
    ConditionDto updateCondition(Long id, JsonPatch patch);
}
