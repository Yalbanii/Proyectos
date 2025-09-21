package com.mongo.crud.rest;

import com.mongo.crud.model.Jugador;
import com.mongo.crud.service.JugadoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@ControllerAdvice
@RestController
@RequestMapping("/api/jugadores") //http://localhost:8080/api/jugadores
public class JugadorRest {

	@Autowired
	private JugadoresService jugadoresService;

	@PostMapping
	public Jugador createJugador(@RequestBody Jugador jugador) {
		return jugadoresService.save(jugador);
	}

	@GetMapping
	public List<Jugador> getAllJugador() {
	    return jugadoresService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Jugador> getJugadorById(@PathVariable String id) {
		try {
			Optional<Jugador> jugador = jugadoresService.findById(id);
			return jugador.map(ResponseEntity::ok)
					.orElse(ResponseEntity.notFound().build());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
	@PutMapping("/{id}")
	public ResponseEntity<Jugador> updateJugador(@PathVariable String id, @RequestBody Jugador jugador) {
		try {
			Jugador updatedJugador = jugadoresService.update(id, jugador);
			return ResponseEntity.ok(updatedJugador);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public Jugador deleteJugador(@PathVariable String id) {
	return jugadoresService.deleteById(id);
}
}
