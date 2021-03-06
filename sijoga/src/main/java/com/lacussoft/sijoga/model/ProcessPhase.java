package com.lacussoft.sijoga.model;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tb_fases_processo")
public class ProcessPhase implements Model, Comparable<ProcessPhase> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "processo", nullable = false)
    private Process process;

    @Column(name = "titulo", nullable = false)
    private String title;

    @Column(name = "descricao")
    private String description;

    @OneToMany(mappedBy = "processPhase")
    private Set<ProcessPhaseAttachment> attachments = new TreeSet<>();

    @Embedded
    private ProcessPhaseResponse response;

    @ManyToOne
    @JoinColumn(name = "criado_por", nullable = false)
    private Advogado createdBy;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "executado_por")
    private Juiz executedBy;

    @Column(name = "executado_em")
    private Date executedAt;

    public ProcessPhase() {}

    public ProcessPhase(Process process, String title, String description, ProcessPhaseResponse response, Advogado createdBy, Juiz executedBy, Date executedAt) {
        this.description = description;
        this.executedAt = executedAt;
        this.executedBy = executedBy;
        this.createdBy = createdBy;
        this.response = response;
        this.process = process;
        this.title = title;
    }

    @Override
    public int compareTo(ProcessPhase phase) {
        return phase.createdAt.compareTo(createdAt);
    }

    public Long getId() {
        return id;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ProcessPhaseAttachment> getAttachments() {
        return attachments;
    }

    public ProcessPhaseResponse getResponse() {
        return response;
    }

    public void setResponse(ProcessPhaseResponse response) {
        this.response = response;
    }

    public Advogado getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Advogado advogado) {
        createdBy = advogado;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Juiz getExecutedBy() {
        return executedBy;
    }

    public void setExecutedBy(Juiz judge) {
        executedBy = judge;
    }

    public Date getExecutedAt() {
        return executedAt;
    }
}
