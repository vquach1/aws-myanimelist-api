package hibernateUtils.hibernateConverters;

import awsUtils.MiscUtils;
import awsUtils.S3Utils;
import hibernateUtils.HibernateUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Converter {
    protected String bucketName;

    @Autowired
    protected HibernateUtils hibernateUtils;

    @Autowired
    protected MiscUtils miscUtils;

    @Autowired
    protected S3Utils s3Utils;

    public Converter(String bucketName) {
        this.bucketName = bucketName;
    }

    public abstract void convert(String key);
}
