import com.mlf.yygh.enums.AuthStatusEnum;

/**
 * Created by Administrator on 2022/4/20.
 */
public class Test1 {
    public static void main(String[] args) {

        String statusNameByStatus = AuthStatusEnum.getStatusNameByStatus(3);
        System.out.println(statusNameByStatus);
    }
}
