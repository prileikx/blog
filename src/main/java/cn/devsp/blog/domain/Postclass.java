package cn.devsp.blog.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Postclass implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pcid", type = IdType.AUTO)
    private Integer pcid;

    private String pcname;

    private Integer limits;

    private String englishname;


}
