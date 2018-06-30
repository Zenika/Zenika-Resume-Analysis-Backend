package fr.zenika.search.zenikaresume.backend.parsing;

import org.joda.time.DateTime;

public class ResumeApiResponse {

    private String content;
    private MetadataRaw metadata;
    private DateTime last_modified;
    private String path;
    private String uuid;
    private String version;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MetadataRaw getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataRaw metadata) {
        this.metadata = metadata;
    }

    public DateTime getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(DateTime last_modified) {
        this.last_modified = last_modified;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ResumeApiResponse{" +
                ", metadata=" + metadata +
                ", last_modified=" + last_modified +
                ", path='" + path + '\'' +
                ", uuid='" + uuid + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
