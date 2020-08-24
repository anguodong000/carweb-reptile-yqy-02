package com.reptile.carwebreptileyqy.entity;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import java.io.Serializable;
import java.util.Date;

@Data
public class AutoPartsInfoEntity implements Serializable {
    private static final long serialVersionUID = -8742074947015792578L;

    @Field("id")
    private String id;

    @Field("productNumber")
    private String productNumber;

    @Field("productName")
    private String productName;

    @Field("vehicleModel")
    private String vehicleModel;

    @Field("wholesalePrice")
    private Double wholesalePrice;

    @Field("retailPrice")
    private Double retailPrice;

    @Field("discountPrice")
    private Double discountPrice;

    @Field("factoryNumber")
    private String factoryNumber;

    @Field("specification")
    private String specification;

    @Field("priceChange")
    private String priceChange;

    private Date createTime;

    private Date updateTime;
}
