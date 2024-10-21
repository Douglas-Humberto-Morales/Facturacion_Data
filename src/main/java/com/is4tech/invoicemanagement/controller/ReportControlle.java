package com.is4tech.invoicemanagement.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.is4tech.invoicemanagement.dto.ReportDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.ReportService;
import com.is4tech.invoicemanagement.utils.Message;
import com.is4tech.invoicemanagement.utils.MessagePage;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/invoice-management/v0.1/report")
public class ReportControlle {
    private final ReportService reportService;

    private static final String NAME_ENTITY = "Report";
    private static final String ID_ENTITY = "no_ordered";
    int statusCode;

    public ReportControlle (ReportService reportService){
        this.reportService = reportService;
    }

    @GetMapping("/show-all")
    public ResponseEntity<Message> showAllReports(
        @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request){
        MessagePage reports = reportService.listAllReport(pageable, request);
        return new ResponseEntity<>(Message.builder()
            .note("Records found")
            .object(reports)
            .build(),
            HttpStatus.OK);
    }

    @GetMapping("/show-by-id/{id}")
    public ResponseEntity<Message> showByIdReports(
        @PathVariable Integer id, HttpServletRequest request){
        ReportDto reportDto = reportService.findByIdReport(id, request);
        if (reportDto == null) {
            throw new ResourceNorFoundException("Report not found with id: " + id);
        }
        return new ResponseEntity<>(Message.builder()
            .note("Record found: " + ID_ENTITY)
            .object(reportDto)
            .build(),
            HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Message> saveReport(@RequestBody ReportDto reportDto, 
        HttpServletRequest request) throws BadRequestException{
        try {
            ReportDto reportDtoSave = reportService.saveReport(reportDto, request);
            statusCode = HttpStatus.CREATED.value();
            return new ResponseEntity<>(Message.builder()
                .note("Record create")
                .object(reportDtoSave)
                .build(),
                HttpStatus.OK);
        } catch (DataAccessException e) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            throw new BadRequestException("Error saving record: " + NAME_ENTITY + e.getMessage());
        } catch (ResourceNorFoundException e) {
            statusCode = HttpStatus.NOT_FOUND.value();
            throw e;
        } catch (Exception e) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            throw new BadRequestException("Unexpected error occurred: " + e.getMessage());
        }
    }
}
