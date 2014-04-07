package com.sgsoft.servicer.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by Viktor Rotar on 03.04.14.
 */
public class Bid {
    private Integer id;
    private Date registerDate;
    private Client client;
    private ProductType productType;
    private String serialNumber;
    private String notes;
    private String defect;
    private Date diagnosticIssueDate;
    private Date diagnosticReturnDate;
    private String diagnosticResult;
    private Boolean isGuarantee;
    private Date termToService;
    private Date serviceIssueDate;
    private Date serviceReturnDate;
    private Date serviceResult;
    private String clientResult;
    private String declarationNumber;
    private String officeNumber;
    private String finalNotes;
    private List<Component> componentList;
    private List<WorkType> workTypeList;
    private List<BidState> bidStates;

    public List<BidState> getBidStates() {
        return bidStates;
    }

    public void setBidStates(List<BidState> bidStates) {
        this.bidStates = bidStates;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDefect() {
        return defect;
    }

    public void setDefect(String defect) {
        this.defect = defect;
    }

    public Date getDiagnosticIssueDate() {
        return diagnosticIssueDate;
    }

    public void setDiagnosticIssueDate(Date diagnosticIssueDate) {
        this.diagnosticIssueDate = diagnosticIssueDate;
    }

    public Date getDiagnosticReturnDate() {
        return diagnosticReturnDate;
    }

    public void setDiagnosticReturnDate(Date diagnosticReturnDate) {
        this.diagnosticReturnDate = diagnosticReturnDate;
    }

    public String getDiagnosticResult() {
        return diagnosticResult;
    }

    public void setDiagnosticResult(String diagnosticResult) {
        this.diagnosticResult = diagnosticResult;
    }

    public Boolean isGuarantee() {
        return isGuarantee;
    }

    public void setGuarantee(Boolean isGuarantee) {
        this.isGuarantee = isGuarantee;
    }

    public Date getTermToService() {
        return termToService;
    }

    public void setTermToService(Date termToService) {
        this.termToService = termToService;
    }

    public Date getServiceIssueDate() {
        return serviceIssueDate;
    }

    public void setServiceIssueDate(Date serviceIssueDate) {
        this.serviceIssueDate = serviceIssueDate;
    }

    public Date getServiceReturnDate() {
        return serviceReturnDate;
    }

    public void setServiceReturnDate(Date serviceReturnDate) {
        this.serviceReturnDate = serviceReturnDate;
    }

    public Date getServiceResult() {
        return serviceResult;
    }

    public void setServiceResult(Date serviceResult) {
        this.serviceResult = serviceResult;
    }

    public String getClientResult() {
        return clientResult;
    }

    public void setClientResult(String clientResult) {
        this.clientResult = clientResult;
    }

    public String getDeclarationNumber() {
        return declarationNumber;
    }

    public void setDeclarationNumber(String declarationNumber) {
        this.declarationNumber = declarationNumber;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public String getFinalNotes() {
        return finalNotes;
    }

    public void setFinalNotes(String finalNotes) {
        this.finalNotes = finalNotes;
    }

    public List<Component> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<Component> componentList) {
        this.componentList = componentList;
    }

    public List<WorkType> getWorkTypeList() {
        return workTypeList;
    }

    public void setWorkTypeList(List<WorkType> workTypeList) {
        this.workTypeList = workTypeList;
    }

    public Double getBidPrice()
    {
        double componentPrice = 0;
        double workPrice = 0;

        for (Component component:componentList)
        {
            componentPrice+=component.getPrice().doubleValue();
        }

        for(WorkType workType:workTypeList)
        {
            workPrice += workType.getPrice().doubleValue();
        }

        return componentPrice+workPrice;
    }
}
