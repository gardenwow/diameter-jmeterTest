/*
package Samler;

import org.jdiameter.api.AvpSet;
import org.jdiameter.api.Message;
import api.ISessionFactory;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.impl.parser.MessageImpl;

public class DiameterMessageExample {
    public static void main(String[] args) {
        // Создайте экземпляр фабрики для сообщений
        SessionFactory messageFactory = new SessionFactory(); // Здесь необходимо получить экземпляр MessageFactory из jdiameter

        // Создайте новое сообщение (это может быть CER, UAR, MAR или другой тип, в зависимости от вашей задачи)
        Message message = messageFactory.getSessionId();

        // Получите AvpSet для добавления атрибутов
        AvpSet avpSet = message.getAvps();

        // Преобразуйте строку в массив байт (если она не была в этом формате)
        String hexData = "0100017C40000110000000042000E0006AB009550000010740000051687561322D67792E7067772E746573747A6F6E652E6570632E6D6E633039392E6D63633235302E336770706E6574776F726B2E6F72673B333834363930343934383B3234343B3931340000000000010C4000000C000007D100000108400000106865726D65732D31000001284000000E6865726D65730000000001A04000000C000000010000019F4000000C00000000000001024000000C00000004000001C8400000B8000001B04000000C000000650000010C4000000C000013A6000001C04000000C0000003C000001AE4000008C000001C14000000C00000001000001B240000078000001B14000000C00000002000001B34000006168747470733A2F2F62616C616E63652E6265656C696E652E72752F3F613D696E7465726E65742E6265656C696E652E7275267A3D475052535F4245454C494E455F525553534941266869696F3D30266972663D302653523D30000000000001AB4000000C00000000";
        byte[] dataBytes = hexStringToByteArray(hexData);

        // Добавьте атрибут AVP с данными
        avpSet.addAvp(YourAvpCode.YOUR_AVP_CODE, dataBytes);
        avpSet.addAvp();

        // Теперь у вас есть сообщение Diameter с добавленным AVP, готовым к отправке.
        // Отправьте сообщение на сервер Diameter, используя вашу клиентскую логику.
    }

    // Метод для преобразования шестнадцатеричной строки в массив байт
    public static byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}*/
