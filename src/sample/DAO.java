package sample;

import java.sql.*;
import java.util.Scanner;

public class DAO {

    private static final String GET_BRANDING_ID_FOR_APP = "select first 1 brand_id from mobile_app_instances where mobile_app_id = ?  AND is_active ='Y'";

    private static final String UPDATE_BRANDING_ID_FOR_CH1 = "update card_programs set branding_id= ?  where card_prg_id in (select card_prg_id from cards where card_no in ( select category_ref from users where user_id = ? ))";

    private static final String UPDATE_BRANDING_ID_FOR_CH2OR3 = "update card_programs set branding_id= ?  where card_prg_id in (select card_prg_id from cards where card_srno in ( select default_card_srno from customers where ch_id in ( select ch_id from users where user_id = ? ) ))";

    private static final String GET_CH_PROFILE_OPTS_FOR_BRAND = "select bps.param_val from  brand_parameters bp, outer brand_params bps where bps.active = 'Y' and bp.param_code = 'CH_PROFILE_OPTION'  and bps.branding_id = ? and bp.param_code = bps.param_code";

    private static final String UPDATE_MBl_LOGIN_OPTS_FOR_BRAND = "update brand_params set param_val='3' where param_code = 'mblLoginOpts' and branding_id = ?";

    private static final String UPDATE_USER_CONFIGS = "update users set is_login_locked ='N', is_fraud_blocked= 'N', last_wrong_tries = 0, role_id = 'Cardholder', user_category ='C',is_login_ok = 'Y', is_disable = 'N', encrypt_user_password = '937e8d5fbb48bd4949536cd65b8d35c426b80d2f830c5c308e2cdec422ae2244', is_active_for_ch = 'Y'  where user_id = ? ";

    //update  system_parameters  set param_value= 'N' where param_id = 'AlwPreLoginRstrctns';

    public static void main1() throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
        Scanner scanner = new Scanner(System.in);
        String[] strArr = new String[100];

        String temp = scanner.nextLine();
        try {
            strArr = temp.split(" ");
            Integer num = Integer.parseInt(strArr[0]);
            Integer[] intArr = new Integer[100];
            for (int i = 0; i < strArr.length; i++) {
                intArr[i] = Integer.parseInt(strArr[i]);
            }
            System.out.println(sum(intArr));

        } catch (Exception e) {
            System.out.println(sum(strArr));
        }


    }

    public static Integer sum(Integer[] ints) {

        //sum the integers
        int j = -1;
        Integer sum = 0;
        while (j++ < ints.length && ints[j] != null) {
            sum += ints[j];
        }
        //return the sum
        return sum;
    }

    public static String sum(String[] ints) {

        //sum the integers

        String sum = "";
        for (int j = 0; j < ints.length; j++) {
            sum += ints[j];
        }
        //return the sum
        return sum;
    }

    public String setUserConfig(String appID, String userID, Connection SI, Connection MI) throws Exception {
        String response = null;
        String brandingID = null;
        if (appID != null && userID != null) {
            brandingID = getBrandingIdForApp(appID, MI);
            if (brandingID != null) {
                if (updateUserConfigs(userID, SI) == 1) {
                    if (updateBrandingIDForChOPT(brandingID, userID, SI) == 1) {
                        if (updateMblLoginOptsForBrand(brandingID, SI) == 1) {
                            response = "00";
                        }
                    }
                }
            }
        }
        return response;
    }

    private int updateUserConfigs(String userID, Connection connection) {
        String s = "He is a very very good boy, isn't he?";
        String[] list = s.split("[A-Za-z !,?._'@]+");
        int result = -1;
        PreparedStatement pstmt = null;
        String query = null;
        ResultSet rst = null;
        try {
            if (userID != null) {
                query = UPDATE_USER_CONFIGS;
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, userID);
                result = pstmt.executeUpdate();
            }
        } catch (SQLException var13) {
        } finally {
            cleanUp(pstmt, rst);
        }
        return result;
    }


    private int updateMblLoginOptsForBrand(String brandingID, Connection connection) {
        int result = -1;
        PreparedStatement pstmt = null;
        String query = null;
        ResultSet rst = null;
        try {
            if (brandingID != null) {
                query = UPDATE_MBl_LOGIN_OPTS_FOR_BRAND;
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, brandingID);
                result = pstmt.executeUpdate();
            }
        } catch (SQLException var13) {
        } finally {
            cleanUp(pstmt, rst);
        }
        return result;
    }

    private int updateBrandingIDForChOPT(String brandingID, String userID, Connection connection) {
        int result = -1;
        String chProfileOPt = null;
        if (brandingID != null && userID != null) {
            chProfileOPt = getChProfileOptsForBrand(brandingID, connection);
            if (chProfileOPt != null) {
                if ("1".equals(chProfileOPt)) {
                    result = updateBrandingFORCH1(brandingID, userID, connection);
                } else {
                    result = updateBrandingFORCH2ORCH3(brandingID, userID, connection);
                }
            } else {
                chProfileOPt = getChProfileOptsForBrand(brandingID, connection);
                if (chProfileOPt != null) {
                    if ("1".equals(chProfileOPt)) {
                        result = updateBrandingFORCH1(brandingID, userID, connection);
                    } else {
                        result = updateBrandingFORCH2ORCH3(brandingID, userID, connection);
                    }
                }
            }

        }
        return result;
    }

    private int updateBrandingFORCH1(String brandingID, String userID, Connection connection) {
        int result = -1;
        PreparedStatement pstmt = null;
        String query = null;
        ResultSet rst = null;
        try {
            if (brandingID != null && userID != null) {
                query = UPDATE_BRANDING_ID_FOR_CH1;
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, brandingID);
                pstmt.setString(2, userID);
                result = pstmt.executeUpdate();

            }
        } catch (SQLException var13) {
        } finally {
            cleanUp(pstmt, rst);
        }
        return result;
    }

    private int updateBrandingFORCH2ORCH3(String brandingID, String userID, Connection connection) {
        int result = -1;
        PreparedStatement pstmt = null;
        String query = null;
        ResultSet rst = null;
        try {
            if (brandingID != null && userID != null) {
                query = UPDATE_BRANDING_ID_FOR_CH2OR3;
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, brandingID);
                pstmt.setString(2, userID);
                result = pstmt.executeUpdate();

            }
        } catch (SQLException var13) {
        } finally {
            cleanUp(pstmt, rst);
        }
        return result;
    }

    private String getBrandingIdForApp(String appID, Connection connection) {
        String brandingID = null;
        PreparedStatement pstmt = null;
        String query = null;
        ResultSet rst = null;
        try {
            if (appID != null) {
                query = GET_BRANDING_ID_FOR_APP;
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, appID);
                rst = pstmt.executeQuery();
                if (rst.next()) {
                    brandingID = rst.getString("brand_id");
                }

            }
        } catch (SQLException var13) {
        } finally {
            cleanUp(pstmt, rst);
        }
        return brandingID;
    }

    private String getChProfileOptsForBrand(String brandingID, Connection connection) {
        String userMode = null;
        PreparedStatement pstmt = null;
        String query = null;
        ResultSet rst = null;
        try {
            if (brandingID != null) {
                query = GET_CH_PROFILE_OPTS_FOR_BRAND;
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, brandingID);
                rst = pstmt.executeQuery();
                if (rst.next()) {
                    userMode = rst.getString("param_val");
                }

            }
        } catch (SQLException var13) {
        } finally {
            cleanUp(pstmt, rst);
        }
        return userMode;
    }

    private void cleanUp(Statement stmt, ResultSet rst) {
        if (rst != null) {
            try {
                rst.close();
            } catch (SQLException var4) {
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException var3) {
            }
        }

    }


}
