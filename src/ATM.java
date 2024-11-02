import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *     ATM类：ATM管理系统，提供所有业务需求
 *     ATM对象负责对账户处理
 * */
public class ATM {
    private ArrayList<Account> accounts = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    Account loginAcc;

    /* 启动ATM系统的方法，欢迎使用界面 */
    public void start(){
        while (true) {
            System.out.println("===欢迎进入ATM系统===");
            System.out.println("1、用户登录");
            System.out.println("2、创建用户");
            System.out.println("请选择您要操作的命令：");
            int command = sc.nextInt();
            switch (command){
                case 1:
                    // 用户登录
                    login();
                    break;
                case 2:
                    // 用户创建
                    createAccount();
                    break;
                default:
                    System.out.println("没有该操作");
            }
        }
    }


    /** 用户登录方法 */
    private void login(){
        System.out.println("系统登录页面");
        // 1. 判断有没有账户
        if(accounts.size() == 0){
            System.out.println("当前系统无账户，请先创建账户");
            return;
        }
        // 2. 有账户，可以进行登录操作
        while (true) {
            System.out.println("请输入您的卡号：");
            String cardId = sc.next();
            // 判断此卡号是否存在
            Account acc = getAccountByCardId(cardId); // 返回的是此卡号的账户对象
            if(acc == null){
                // 卡号不存在
                System.out.println("您输入的卡号有误，请重新输入！");
            }else{
                // 卡号存在
                while (true) {
                    System.out.println("请输入您的密码：");
                    String passWord = sc.next();
                    // 判断密码
                    if(acc.getPassword().equals(passWord)){
                        loginAcc = acc;
                        System.out.println("欢迎" + acc.getUserName() + "，您的卡号为：" + cardId);
                        // 登录后的功能
                        showUserCommand();
                        return; // 跳出并结束当前登录方法
                    }else{
                        System.out.println("密码有误，请重新输入");
                    }
                }

            }
        }
    }

    /** 用户登录后的操作界面 */
    private void showUserCommand(){
        while (true) {
            System.out.println("===欢迎您"+ loginAcc.getUserName() +"您可以办理以下业务===");
            System.out.println("1.查询账户");
            System.out.println("2.存款");
            System.out.println("3.取款");
            System.out.println("4.转账");
            System.out.println("5.修改密码");
            System.out.println("6.退出");
            System.out.println("7.注销账户");
            System.out.println("请选择：");
            int command = sc.nextInt();
            switch (command){
                case 1:
                    // 查询当前账户
                    showLoginAccount();
                    break;
                case 2:
                    // 存款
                    depositMoney();
                    break;
                case 3:
                    // 取款
                    drawMoney();
                    break;
                case 4:
                    // 转账
                    transferMoney();
                    break;
                case 5:
                    // 修改密码
                    updataPassWord();
                    return;
                case 6:
                    // 退出
                    System.out.println(loginAcc.getUserName() + "您退出系统成功");
                    return; // 跳出并结束当前方法
                case 7:
                    // 注销当前账户
                    if(deleteAccount()){
                        // 销户成功,回到欢迎界面
                        return; // 跳出并结束当前方法
                    }
                    break;
                default:
                    System.out.println("没有该操作！");
            }
        }
    }

    /** 修改密码方法 */
    private void updataPassWord() {
        System.out.println("===账户密码修改操作===");
        // 1. 认证当前密码
        while (true) {
            System.out.println("请您输入当前密码：");
            String passWord = sc.next();
            // 2. 认证当前密码是否正确
            if(loginAcc.getPassword().equals(passWord)){
                // 认证通过
                while (true) {
                    // 3. 开始修改密码
                    System.out.println("请您输入新的密码：");
                    String newpassWord = sc.next();

                    System.out.println("请您确认新的密码：");
                    String oknewpassWord = sc.next();

                    // 判断两次密码是否一致
                    if(oknewpassWord.equals(newpassWord)){
                        loginAcc.setPassword(newpassWord);
                        System.out.println("修改密码成功！");
                        return;
                    }else{
                        System.out.println("2次密码不一致，请重新输入！");
                    }
                }
            }else{
                System.out.println("密码不一致，请重新输入！");
            }
        }

    }

    /** 注销账户方法 */
    private boolean deleteAccount() {
        // 1 是否确定销户
        System.out.println("是否确定要销户？y/n?");
        String command = sc.next();
        switch (command){
            case "y":
                // 确认销户
                if(loginAcc.getMoney() == 0){
                    accounts.remove(loginAcc);
                    System.out.println("您好，您的账户已经成功销户");
                    return true;
                }else{
                    System.out.println("账户里还有余额，销户失败");
                    return false;
                }
            default:
                System.out.println("好的，您的账户保留");
                return false;
        }

    }

    /** 转账功能方法 */
    private void transferMoney() {
        System.out.println("===转账操作===");
        // 1 判断系统中存在多个账户
        if(accounts.size() < 2){
            System.out.println("当前账户只有你一个用户，无法给其他账户转账");
            return;
        }
        // 2 判断自己账户中是否有钱
        if(loginAcc.getMoney() == 0){
            System.out.println("余额为零，转不了！");
            return;
        }
        // 3 满足了转账的条件
        while (true) {
            System.out.println("请您输入对方的卡号：");
            String cardId = sc.next();
            Account acc = getAccountByCardId(cardId);
            if(acc == null){
                System.out.println("没有此卡号，请重新输入！");
            }else{
                // 判断户主的姓氏
                // * + 名
                String name = "*" + acc.getUserName().substring(1);
                System.out.println("请您输入【" + name + "】姓氏");
                String preName = sc.next();
                // 4 判断姓氏是否正确
                if(acc.getUserName().startsWith(preName)){
                    while (true) {
                        // 认证通过
                        System.out.println("输入转账金额");
                        double money = sc.nextDouble();
                        // 5 判断金额是否超过自己的余额
                        if(loginAcc.getMoney() >= money){
                            // 没超过,可以转
                            //更新自己账户余额
                            loginAcc.setMoney(loginAcc.getMoney() - money);
                            //更新对方账户余额
                            acc.setMoney(acc.getMoney() + money);
                            System.out.println("您转账成功了");
                            return; //跳出转账方法
                        }else{
                            System.out.println("余额不足，无法转账");
                        }
                    }
                }else{
                    System.out.println("认证失败，姓氏有误");
                }

            }
        }

    }

    /** 取钱功能方法 */
    private void drawMoney() {
        System.out.println("===取钱操作===");
        // 余额有没有大于100
        if(loginAcc.getMoney() < 100){
                System.out.println("余额不足100元，不允许取钱");
                return;
        }

        while (true) {
            System.out.println("请输入要取出的金额：");
            double money = sc.nextDouble();
            if( loginAcc.getMoney() < money){
                System.out.println("余额不足，当前余额为"+loginAcc.getMoney()+"请重新输入：");
            }else{
                // 判断取出来的钱有没有超出限额
                if(money > loginAcc.getLimit()){
                    System.out.println("超出每次限额，您每次最多取出"+ loginAcc.getLimit());
                }else{
                    loginAcc.setMoney(loginAcc.getMoney() - money);
                    System.out.println("取钱成功，取出" + money + "余额为" + loginAcc.getMoney());
                    break;
                }
            }
        }

    }

    /** 存款功能方法 */
    private void depositMoney() {
        while (true) {
            System.out.println("===存钱操作===");
            System.out.println("请输入要存入的金额：");
            double money = sc.nextDouble();
            if(money > 0){
                // 更新账户余额
                loginAcc.setMoney(loginAcc.getMoney() + money);
                System.out.println("存钱成功，" + money + "余额为：" + loginAcc.getMoney());
                break;
            }else{
                System.out.println("输入的金额有误！");
            }
        }
    }

    /** 当前用户登录的信息 */
    private void showLoginAccount() {
        System.out.println("当前账户信息如下：");
        System.out.println("卡号：" + loginAcc.getCardId());
        System.out.println("姓名：" + loginAcc.getUserName());
        System.out.println("性别：" + loginAcc.getSex());
        System.out.println("余额：" + loginAcc.getMoney());
        System.out.println("取现额度：" + loginAcc.getLimit());
    }


    /** 用户创建方法 */
    private void createAccount(){
        System.out.println("账号创建页面");

        /* 1. 创建账户对象 */
        Account acc = new Account();

        /* 2. 输入账户信息，并赋值给账户对象 */

        // 账户名称
        System.out.println("请输入账户名称：");
        String name = sc.next();
        acc.setUserName(name);

        // 客户性别
        while (true) {
            System.out.println("请输入您的性别：");
            char sex = sc.next().charAt(0);
            if(sex == '男' || sex == '女'){
                acc.setSex(sex);
                break;
            }else{
                System.out.println("输入性别有误！输入的性别只能是男或女");
            }
        }

        // 账户密码
        while (true) {
            System.out.println("请输入您的账户密码：");
            String passWord = sc.next();
            System.out.println("请确认您的账户密码：");
            String okPassWord = sc.next();
            if(okPassWord.equals(passWord)){
                acc.setPassword(passWord);
                break;
            }else{
                System.out.println("您两次输入的密码不一致，请重新输入！");
            }
        }

        // 账户取现额度
        while (true) {
            System.out.println("请输入您的取现额度：");
            double limit = sc.nextDouble();
            if(limit > 0){
                acc.setLimit(limit);
                break;
            }else{
                System.out.println("输入的取现额度有误");
            }
        }


        // 账户卡号是自动生成的,长度为8位，不能重复
        String newCardId = createCardId();
        acc.setCardId(newCardId);


        /* 3. 把账户存入账户集合 */
        accounts.add(acc);
        System.out.println("恭喜您：" + acc.getUserName() + "账户创建成功，您的卡号是：" + acc.getCardId());
    }

    /** 创建卡号方法
     * 返回一个8位数字的卡号，而且不能重复 */
    private String createCardId(){
        while (true) {
            String cardId = "";
            Random r = new Random();
            for (int i = 0; i < 8; i++) {
                int data = r.nextInt(10); // 0-9
                cardId += data;
            }

            // 判断卡号是否重复
            Account acc = getAccountByCardId(cardId);
            if(acc == null){
                return cardId;
            }
        }
    }

    /**
     *  判断卡号是否重复方法
     *  通过卡号去找账户，找到了就说明此卡号重复，没有找到账户就说明还没有账户有此卡号
     * */
    private Account getAccountByCardId(String cardId){
        for (int i = 0; i < accounts.size(); i++) {
            //遍历accounts 账户的全部对象，并赋值给变量acc
            Account acc = accounts.get(i);
            if(acc.getCardId().equals(cardId)){
                return acc;
            }
        }
        return null; // 没有这个账户,代表这个卡号没有重复
    }

}
