package cn.devsp.blog.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;

    private String username;

    private String describes;
    @TableField(select = false)
    private String upassword;

    private Integer ugid;
    @TableField(select = false)
    private String email;
    @TableField(select = false)
    private String verify;

    private String registertime;

    private Integer ifconfirmemail;

    private Integer replycount;

    private Integer postcount;

    private String img;


}
