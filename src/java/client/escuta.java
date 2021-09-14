/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author Vinicius
 */
public class escuta implements MessageListener{
    
    @Override
    public void onMessage(Message message) {
         try {
             TextMessage textMsg = (TextMessage) message;
             String text = textMsg.getText();
             System.out.println(text);
         } catch (JMSException ex) {
         }
    }
}
