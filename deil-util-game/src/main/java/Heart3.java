/**
 * @PURPOSE heart3
 * @DATE 2022/12/06
 * @CODE Deil
 */
public class Heart3 {

    public /*static*/ void main(String[] args) throws InterruptedException {
        int count = 0;
        for (float y = 2.5f; y > -2.0f; y -= 0.12f) {
            for (float x=-2.3f;x<2.3f;x+=0.041f){
                float a = x*x+y*y-4f;
                if((a*a*a-x*x*y*y*y)<-0.0f){
                    String str ="I LOVE YOU";
                    int mun = count%str.length();
                    System.out.print(str.charAt(mun));
                    count++;
                }else{
                    System.out.print(" ");
                }
            }
            System.out.println();
            Thread.sleep(100);
        }
        System.out.println("可以怀疑是否爱你");
        System.out.println("但请不要担心是否爱你");
    }

}
