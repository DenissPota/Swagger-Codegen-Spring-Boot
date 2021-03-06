package io.swagger.api;

import ee.helmes.entities.WorkerJPA;
import ee.helmes.exceptions.WorkerNotFoundException;
import ee.helmes.mapper.WorkerMapper;
import ee.helmes.repository.WorkerRepository;
import io.swagger.model.Worker;
import io.swagger.model.Workers;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-12-04T10:23:19.831Z[GMT]")

@RestController
public class WorkersApiController implements WorkersApi {

    private static final Logger log = LoggerFactory.getLogger(WorkersApiController.class);
    private ObjectMapper objectMapper;
    private HttpServletRequest request;
    private final WorkerRepository workerRepository;

    @Autowired
    public WorkersApiController(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }


    public ResponseEntity<Workers> listAllWorkers(@ApiParam(value = "How many items to return at one time (max 100)") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        Workers workers = new Workers();
        WorkerMapper mapper;
        Integer amountOfWorkers = Math.toIntExact(workerRepository.count());
        Integer amountToShow = amountOfWorkers;
        if (limit != null) {
            amountToShow = (limit <= amountOfWorkers) ? limit : amountOfWorkers;
        }
        for (int i = 0; i < amountToShow; i++) {
            workers.add(WorkerMapper.INSTANCE.workerJPAToWorker(workerRepository.findAll().get(i)));
        }

        return new ResponseEntity<Workers>(workers, HttpStatus.OK);
    }

    public ResponseEntity<Worker> listWorkerById(@Min(1L) @ApiParam(value = "", required = true, allowableValues = "") @PathVariable("workerId") Long workerId) {
//        String accept = request.getHeader("Accept");
        Worker worker = null;
        try {
            worker = WorkerMapper.INSTANCE.workerJPAToWorker(workerRepository
                    .findById(workerId).orElseThrow(() -> new WorkerNotFoundException(workerId)));
        } catch (WorkerNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Worker>(worker, HttpStatus.OK);
    }

    public ResponseEntity<Worker> addWorker(@ApiParam(value = "Pet object that needs to be added to the store", required = true) @Valid @RequestBody Worker body) {
        //  String accept = request.getHeader("Accept");
        WorkerJPA workerJPA = WorkerMapper.INSTANCE.workerToWorkerJPA(body);
        workerRepository.save(workerJPA);
        return new ResponseEntity<Worker>(HttpStatus.CREATED);
    }
}
