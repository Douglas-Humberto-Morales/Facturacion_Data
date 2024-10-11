package com.is4tech.invoicemanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.is4tech.invoicemanagement.dto.ReportDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.model.Customer;
import com.is4tech.invoicemanagement.model.Report;
import com.is4tech.invoicemanagement.repository.CustomerRepository;
import com.is4tech.invoicemanagement.repository.ReportRepository;
import com.is4tech.invoicemanagement.utils.MessagePage;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final CustomerRepository customerRepository;
    
    private static final String NAME_ENTITY = "Report";
    private static final String ID_ENTITY = "no_ordered";

    @Transactional
    public MessagePage listAllReport(Pageable pageable, HttpServletRequest request){
        Page<Report> listReport = reportRepository.findAll(pageable);

        /*
        if(listReport.isEmpty()){
            int statusCode = HttpStatus.NOT_FOUND.value();
            auditService.logAudit(null, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
            throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, pageable.toString());
        }

        int statusCode = HttpStatus.OK.value();
        auditService.logAudit(listReport.getContent(), this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
        */

        return MessagePage.builder()
            .note("Reports Retrieved Successfully")
            .object(listReport.getContent().stream().map(this::toDtoReport).toList())
            .totalElements((int) listReport.getTotalElements())
            .totalPages(listReport.getTotalPages())
            .currentPage(listReport.getNumber())
            .pageSize(listReport.getSize())
            .build();
    }

    @Transactional
    public ReportDto findByIdReport(Integer id, HttpServletRequest request){
        ReportDto reportDto = reportRepository.findById(id)
            .map(this::toDtoReport)
            .orElseThrow(() -> new RuntimeException("Report not found"));

        //int statusCode = HttpStatus.OK.value();

        return reportDto;
    }
    
    public boolean existsById(Integer id) {
        return reportRepository.existsById(id);
    }

    @Transactional
    public ReportDto saveReport(ReportDto reportDto, HttpServletRequest request){
        try {
            Report report = toModelReport(reportDto);
            Report reportSave = reportRepository.save(report);

            //int statusCode = HttpStatus.CREATED.value();
            //auditService.logAudit(reportDto, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);

            return toDtoReport(reportSave);
        } catch (Exception e) {
            //int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            //auditService.logAudit(reportDto, this.getClass().getMethods()[0], e, statusCode, NAME_ENTITY, request);
            throw e;
      }
    }

    private ReportDto toDtoReport(Report report){
        return ReportDto.builder()
            .noOrdered(report.getNoOrdered())
            .amount(report.getAmount())
            .taxes(report.getTaxes())
            .total(report.getTotal())
            .customerId(report.getCustomer().getCustomerId())
            .build();
    }

    private Report toModelReport(ReportDto reportDto){
        Customer customer = customerRepository.findById(reportDto.getCustomerId())
        .orElseThrow(() -> new ResourceNorFoundException("Profile not found"));
        return Report.builder()
            .noOrdered(reportDto.getNoOrdered())
            .amount(reportDto.getAmount())
            .taxes(reportDto.getTaxes())
            .total(reportDto.getTotal())
            .customer(customer)
            .build();
    }
}
