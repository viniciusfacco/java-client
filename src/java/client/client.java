package client;

/**
 *
 * @author vinicius.rodrigues
 */

import java.util.Properties;
import java.util.Scanner;
import javax.jms.*;
import javax.naming.*;

public class client{
    
    private static TopicConnection conn;

    public static void init(String nickname) throws NamingException, JMSException {
        
        escuta monitor = new escuta();
        
        Properties env = new Properties();
        env.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory"); 
        env.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming"); 
        env.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl"); 
        env.setProperty("org.omg.CORBA.ORBInitialHost", "127.0.0.1"); 
        env.setProperty("org.omg.CORBA.ORBInitialPort", "3700");        
        Context jndi = new InitialContext(env);

        System.out.println("instanciando connectionfactory");
        TopicConnectionFactory connfact = (TopicConnectionFactory) jndi.lookup("TopicConnectionFactory");
        
        System.out.println("instanciando topic");
        Topic tp = (Topic) jndi.lookup("vini");
        
        System.out.println("instanciando connection");
        conn = connfact.createTopicConnection();
        
        System.out.println("instanciando session");
        TopicSession sess = conn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        
        System.out.println("instanciando topicsubsciber");
        TopicSubscriber tps = sess.createSubscriber(tp);
        tps.setMessageListener(monitor);
        
        System.out.println("iniciando conexão");
        conn.start();
        
        System.out.println("criando messageproducer");
        MessageProducer msgprd = sess.createProducer(tp);
        
        System.out.println("criando mensagem");
        TextMessage msg = sess.createTextMessage();
                   
        System.out.println("Digite a mensagem");
        Scanner commandLine = new Scanner(System.in);
        String s = commandLine.nextLine();
        
        while (!s.equalsIgnoreCase("exit")){            
            msg.setText(s);
            msgprd.send(msg);
            s = commandLine.nextLine();
        }
        
        conn.close();
        //System.out.println("criando messageconsumer");
        //MessageConsumer msgcsm = sess.createConsumer(tp);
        
        //System.out.println("aguardando dados");
        //TextMessage msg = (TextMessage) msgcsm.receive();
        
        //System.out.println("lendo mensagem");
        //String s = msg.getText();
        
        //System.out.println("dados recebidos: " + s);
        //while (!s.equalsIgnoreCase("exit")){
        //    System.out.println("aguardando dados");
        //    msg = (TextMessage) msgcsm.receive();
        //    System.out.println("lendo mensagem");
        //    s = msg.getText();
        //    System.out.println("dados recebidos: " + s);
        //}
    }
    
    public static void main(String[] args) throws NamingException, JMSException {
        Scanner commandLine = new Scanner(System.in);
        System.out.print("Digite seu nickname: ");
        String nick = commandLine.nextLine();
        if (nick.isEmpty()){
            System.out.println("Nome inválido.");
        } else{
            init(nick);
        }
    }
}
