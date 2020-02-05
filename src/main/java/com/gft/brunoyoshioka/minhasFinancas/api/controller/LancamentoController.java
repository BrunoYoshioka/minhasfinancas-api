package com.gft.brunoyoshioka.minhasFinancas.api.controller;

import com.gft.brunoyoshioka.minhasFinancas.api.dto.LancamentoDTO;
import com.gft.brunoyoshioka.minhasFinancas.exception.RegraNegocioException;
import com.gft.brunoyoshioka.minhasFinancas.model.entity.Lancamento;
import com.gft.brunoyoshioka.minhasFinancas.model.entity.Usuario;
import com.gft.brunoyoshioka.minhasFinancas.model.enums.StatusLancamento;
import com.gft.brunoyoshioka.minhasFinancas.model.enums.TipoLancamento;
import com.gft.brunoyoshioka.minhasFinancas.service.LancamentoService;
import com.gft.brunoyoshioka.minhasFinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;
    private final UsuarioService usuarioService;

    /*public LancamentoController(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
        this.usuarioService = usuarioService;
    }*/

    @GetMapping
    public ResponseEntity buscar(

            //@RequestParam java.util.Map<String, String> params

            @RequestParam(value = "descricao" , required = false) String descricao,
            @RequestParam(value = "mes" , required = false) Integer mes,
            @RequestParam(value = "ano" , required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario
            ) {
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);

        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if (usuario.isPresent()) {
            return ResponseEntity.badRequest().body("Não foi possível realizar a consulta." +
                    " Usuário não encontrado para o Id informado.");
        } else {
            lancamentoFiltro.setUsuario(usuario.get());
        }

        List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);
    }


    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO lancamentoDTO){
        try{
            Lancamento entidade = converter(lancamentoDTO);
            entidade = lancamentoService.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar (@PathVariable("id") Long id, @RequestBody LancamentoDTO dto){
        return lancamentoService.obterPorId(id).map( entity -> {
            try{
                Lancamento lancamento = converter(dto);
                lancamento.setId(entity.getId());
                lancamentoService.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            } catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet( () ->
                new ResponseEntity("Lançamento não encontrado na base de dados" , HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id){
        return lancamentoService.obterPorId(id).map( entidade -> {
            lancamentoService.deletar(entidade);
            return new ResponseEntity( HttpStatus.NO_CONTENT );
        }).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de dados" , HttpStatus.BAD_REQUEST));
    }

    private Lancamento converter (LancamentoDTO dto){
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setMes(dto.getMes());
        lancamento.setAno(dto.getAno());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService
                .obterPorId(dto.getUsuario())
                .orElseThrow( () -> new RegraNegocioException("Usuário não encontrado para o Id informado"));

        lancamento.setUsuario(usuario);
        if(dto.getTipo() != null){
            lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        }

        if(dto.getStatus() != null){
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }
        return lancamento;
    }
}
