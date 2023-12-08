package cn.devsp.blog.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
public class Captcha implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ctid", type = IdType.AUTO)
    private Integer ctid;

    private Integer uid;

    private String email;

    private String captcha;

    private LocalDateTime sendtime;


}
