package br.com.sw2you.realmeet.report.model;

import br.com.sw2you.realmeet.email.TemplateType;
import br.com.sw2you.realmeet.report.enumeration.ReportFormat;
import java.util.Arrays;
import java.util.Objects;

public class GeneratedReport {
    private final String email;
    private final byte[] bytes;
    private final ReportFormat reportFormat;
    private final String fileName;
    private final TemplateType templateType;

    private GeneratedReport(Builder builder) {
        this.bytes = builder.bytes;
        this.reportFormat = builder.reportFormat;
        this.fileName = builder.fileName;
        this.templateType = builder.templateType;
        this.email = builder.email;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public ReportFormat getReportFormat() {
        return reportFormat;
    }

    public String getFileName() {
        return fileName;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneratedReport that = (GeneratedReport) o;
        return Arrays.equals(bytes, that.bytes) && reportFormat == that.reportFormat && Objects.equals(fileName, that.fileName) && templateType == that.templateType;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(reportFormat, fileName, templateType);
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }

    @Override
    public String toString() {
        return "GeneratedReport{" +
                "email='" + email + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                ", reportFormat=" + reportFormat +
                ", fileName='" + fileName + '\'' +
                ", templateType=" + templateType +
                '}';
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String email;
        private byte[] bytes;
        private ReportFormat reportFormat;
        private String fileName;
        private TemplateType templateType;

        private Builder() {}

        public Builder bytes(byte[] bytes) {
            this.bytes = bytes;
            return this;
        }

        public Builder reportFormat(ReportFormat reportFormat) {
            this.reportFormat = reportFormat;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder templateType(TemplateType templateType) {
            this.templateType = templateType;
            return this;
        }

        public GeneratedReport build() {
            return new GeneratedReport(this);
        }
    }
}
