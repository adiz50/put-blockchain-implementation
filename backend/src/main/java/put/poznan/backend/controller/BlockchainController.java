package put.poznan.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import put.poznan.backend.entities.Block;
import put.poznan.backend.service.BlockchainService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/blockcahin")
@RequiredArgsConstructor
public class BlockchainController {

    private final BlockchainService blockchainService;

    @GetMapping
    public ResponseEntity< List< Block > > getBlockchain() {
        return ResponseEntity.ok( blockchainService.getBlockchain() );
    }
}
