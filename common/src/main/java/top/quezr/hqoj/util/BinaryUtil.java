package top.quezr.hqoj.util;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/21 9:44
 */
public class BinaryUtil {

    /**
     * 取第i位的值
     * <p>
     * 如18的二进制为10010,要获取第3位,则右移(3-1)位得100,第2位变成第1位,这样就可以和&1,最低位为1返回1,为0则返回0
     *
     * @param num   整数
     * @param index 低位起第几位下标
     * @return
     */
    public static int getStatusType(int num, int index) {
        return (num >> (index - 1) & 1);
    }

    /**
     * @param num    整数
     * @param index  低位起第几位下标
     * @param status 要修改的状态
     * @return
     */
    public static int updateStatusType(int num, int index, int status) {
        if (status != 0 && status != 1) {
            throw new IllegalArgumentException();
        }
        if (status == 1) {
            //1向左移(index-1) 和10010 或
            return (1 << (index - 1)) | num;
        } else {
            //先判断原来是不是0,原来是0则直接返回
            if (getStatusType(num,index)==0){
                return num;
            }
            //10010 - 1向左移(index-1)
            return num - (1 << (index - 1));
        }

    }

}
