package com.emented.weblab4.controller;

import com.emented.weblab4.DTO.HitCheckDTO;
import com.emented.weblab4.DTO.SuccessMessageDTO;
import com.emented.weblab4.sequrity.service.UserDetailsImpl;
import com.emented.weblab4.service.hit.HitService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HitApiController {

    private final HitService hitService;

    @Autowired
    public HitApiController(HitService hitService) {
        this.hitService = hitService;
    }

    @PostMapping("/hits")
    private ResponseEntity<SuccessMessageDTO> checkHit(@RequestBody @NotNull @Valid HitCheckDTO hitCheckDTO,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {

        hitService.checkHit(hitCheckDTO, userDetails.getUserId());

        SuccessMessageDTO successMessageDTO = new SuccessMessageDTO("Hit check successful!");
        return ResponseEntity.ok().body(successMessageDTO);
    }

    @DeleteMapping("/hits")
    private ResponseEntity<SuccessMessageDTO> deleteHits(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        hitService.deleteHits(userDetails.getUserId());

        SuccessMessageDTO successMessageDTO = new SuccessMessageDTO("Delete hit checks successful!");
        return ResponseEntity.ok().body(successMessageDTO);
    }

    @GetMapping("/hits")
    private ResponseEntity<List<HitCheckDTO>> getHits(@PathVariable(required = false) Double radius,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<HitCheckDTO> result;

        if (radius == null) {
            result = hitService.getAllHitsForUser(userDetails.getUserId());
        } else {
            result = hitService.getHitsForUserByRadius(userDetails.getUserId(), radius);
        }

        return ResponseEntity.ok().body(result);
    }


}
