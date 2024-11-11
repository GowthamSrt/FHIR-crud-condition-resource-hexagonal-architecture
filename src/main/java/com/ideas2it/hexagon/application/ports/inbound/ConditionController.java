package com.ideas2it.hexagon.application.ports.inbound;

import com.github.fge.jsonpatch.JsonPatch;
import com.ideas2it.hexagon.application.dto.ConditionDto;
import com.ideas2it.hexagon.domain.service.ConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/conditions")
@RequiredArgsConstructor
public class ConditionController {

    private final ConditionService conditionService;

    @PostMapping
    public ResponseEntity<ConditionDto> addCondition(@RequestBody ConditionDto conditionDto) {
        return new ResponseEntity<>(conditionService.addCondition(conditionDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConditionDto> getConditionById(@PathVariable Long id) {
        return new ResponseEntity<>(conditionService.getConditionById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeCondition(@PathVariable Long id) {
        conditionService.removeCondition(id);
        return new ResponseEntity<>("Deleted Successfully!", HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ConditionDto> updateCondition(@PathVariable Long id, @RequestBody JsonPatch patch) {
        ConditionDto updatedCondition = conditionService.updateCondition(id, patch);
        return new ResponseEntity<>(updatedCondition, HttpStatus.OK);
    }
}
