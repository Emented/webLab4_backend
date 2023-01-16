package com.emented.weblab4.controller;

import com.emented.weblab4.DTO.HitCheckDTO;
import com.emented.weblab4.DTO.SuccessMessageDTO;
import com.emented.weblab4.role.HasRole;
import com.emented.weblab4.sequrity.jwt.BearerUser;
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
                                                         @AuthenticationPrincipal BearerUser bearerUser) {

        hitService.checkHit(hitCheckDTO, bearerUser.getUserId());

        SuccessMessageDTO successMessageDTO = new SuccessMessageDTO("Hit check successful!");
        return ResponseEntity.ok().body(successMessageDTO);
    }

    @DeleteMapping("/hits")
    protected ResponseEntity<SuccessMessageDTO> deleteHits(@AuthenticationPrincipal BearerUser bearerUser) {

        hitService.deleteHits(bearerUser.getUserId());

        SuccessMessageDTO successMessageDTO = new SuccessMessageDTO("Delete hit checks successful!");
        return ResponseEntity.ok().body(successMessageDTO);
    }

    @GetMapping("/hits")
    protected ResponseEntity<List<HitCheckDTO>> getHitsByR(@RequestParam Double radius,
                                                           @AuthenticationPrincipal BearerUser bearerUser) {
        List<HitCheckDTO> result;

        if (radius == null) {
            result = hitService.getAllHitsForUser(bearerUser.getUserId());
        } else {
            result = hitService.getHitsForUserByRadius(bearerUser.getUserId(), radius);
        }


        return ResponseEntity.ok().body(result);
    }


}
