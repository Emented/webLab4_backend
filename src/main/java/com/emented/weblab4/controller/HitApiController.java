package com.emented.weblab4.controller;

import com.emented.weblab4.DTO.HitCheckDTO;
import com.emented.weblab4.DTO.SuccessMessageDTO;
import com.emented.weblab4.role.HasRole;
import com.emented.weblab4.sequrity.bearer.CustomBearerUser;
import com.emented.weblab4.service.hit.HitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HitApiController {

    private final HitService hitService;

    @Autowired
    public HitApiController(HitService hitService) {
        this.hitService = hitService;
    }

    @HasRole("DIRECTOR_ROLE")
    @PostMapping("/hits")
    protected ResponseEntity<SuccessMessageDTO> checkHit(@RequestBody @NotNull @Valid HitCheckDTO hitCheckDTO,
                                                         @AuthenticationPrincipal CustomBearerUser customBearerUser) {

        hitService.checkHit(hitCheckDTO, customBearerUser.getUserId());

        SuccessMessageDTO successMessageDTO = new SuccessMessageDTO("Hit check successful!");
        return ResponseEntity.ok().body(successMessageDTO);
    }

    @DeleteMapping("/hits")
    protected ResponseEntity<SuccessMessageDTO> deleteHits(@AuthenticationPrincipal CustomBearerUser customBearerUser) {

        hitService.deleteHits(customBearerUser.getUserId());

        SuccessMessageDTO successMessageDTO = new SuccessMessageDTO("Delete hit checks successful!");
        return ResponseEntity.ok().body(successMessageDTO);
    }

    @GetMapping("/hits")
    protected ResponseEntity<List<HitCheckDTO>> getHitsByR(@RequestParam Double radius,
                                                           @AuthenticationPrincipal CustomBearerUser customBearerUser) {
        List<HitCheckDTO> result;

        if (radius == null) {
            result = hitService.getAllHitsForUser(customBearerUser.getUserId());
        } else {
            result = hitService.getHitsForUserByRadius(customBearerUser.getUserId(), radius);
        }


        return ResponseEntity.ok().body(result);
    }


}
