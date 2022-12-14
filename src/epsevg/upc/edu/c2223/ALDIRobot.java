/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package epsevg.upc.edu.c2223;

import java.awt.Color;
import java.awt.Graphics2D;
import robocode.HitByBulletEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import java.io.Serializable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import robocode.MessageEvent;
import java.util.*;
import java.util.Iterator;
import java.util.List;
//import robocode.util;
import java.lang.Math;
import java.util.concurrent.TimeUnit;
import robocode.Condition;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.Rules;
import robocode.util.Utils;

/**
 *
 * @author Alberto
 */
public class ALDIRobot extends TeamRobot {
    private static TreeMap<Double, String> t_corner0 = new TreeMap<Double, String>();
    private static TreeMap<Double, String> t_corner1 = new TreeMap<Double, String>();
    private static TreeMap<Double, String> t_corner2 = new TreeMap<Double, String>();
    private static TreeMap<Double, String> t_corner3 = new TreeMap<Double, String>();

    // Provisional
    private static double distancia_t1 = 0.0;
    private static double distancia_t2 = 0.0;
    private static double distancia_t3 = 0.0;
    private static double distancia_t4 = 0.0;

    private double angulo = 0.0;
    private static String kamikaze;

    static Double X_TankPos, Y_TankPos;
    static Boolean OnCorner = false;

    // arrays de posiciones
    LinkedList<node> t1 = new LinkedList<node>();
    LinkedList<node> t2 = new LinkedList<node>();
    LinkedList<node> t3 = new LinkedList<node>();
    LinkedList<node> t4 = new LinkedList<node>();
    LinkedList<node> t5 = new LinkedList<node>();

    LinkedList<node> distArray = new LinkedList<node>();

    LinkedList<node> enemigos = new LinkedList<node>();
    LinkedList<String> list_enemigos = new LinkedList<String>();
    static node enemigo_cercano;
    static boolean encontrado = false, estoy_muerto = false, he_llegado = false, llenos = false;
    static long time_initi, time_execute;
    static int cantonada_asignada;

    // --------------------------------------------------------------
    static node EnemigoRecibido;

    static int estado = 0, estado_scanned = 0;
    static private double e_bearing;
    
    //declaracion variables case3
   

    /**
     *
     * @param event
     */

    public void print(LinkedList<node> a) {
        for (int i = 0; i < a.size(); i++) {
            System.out.print(" - " + a.get(i).distancia + " & " + a.get(i).name);
        }
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        if (event.getBearing() > -90 && event.getBearing() <= 90) {
            back(150);
        } else {
            ahead(150);
        }
    }
    

    // @Override
    // public void onHitWall(HitWallEvent event) {
    // out.println("Ouch, I hit a wall bearing " + event.getBearing() + "
    // degrees.");
    // ahead(100);
    // back(100);
    // }

    public void run() {
        execute();
        setAllColors(Color.RED);
        setAdjustGunForRobotTurn(false);
        setAdjustRadarForRobotTurn(false);
        setAdjustRadarForGunTurn(false);
        setGunColor(Color.black);
        setRadarColor(Color.BLUE);
        setScanColor(Color.green);

        // estado = 0;

        System.out.println("~~ENTRANDO EN SWITCH~~");
        // while(estado < 2){
        //while(true){
            switch (estado) {
                case 0: // comunicar las distancias a mis compareos

                    System.out.println("Angulo de giro: " + getRadarHeading());


                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Angulo Restante2: " + getRadarTurnRemaining());
                    setTurnRadarRight(450); // Para que gire 360 grados porque gira 90 grados menos....

                    execute();
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Angulo Restante2: " + getRadarTurnRemaining());
                    // System.out.println("Angulo Restante: "+getRadarTurnRemaining());
                    System.out.println("Angulo de radar: " + getRadarHeading());
                    while (getRadarTurnRemaining() != 0.0) {

                        setTurnRadarRight(getRadarTurnRemaining());
                        execute();
                        System.out.println(
                                ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Angulo Restante2: " + getRadarTurnRemaining());
                        System.out.println("------------------------->Angulo de radar: " + getRadarHeading());
                    }

                    break;

                case 1: // asignaci??n de enemigo //hacer pruebas primero con MyFirstDroid, Corner,
                        // TeamCorner, MyFirstTeam

                    asignarEnemigo();
                    break;

                case 2: // Tracking
                    System.out.println("Tuesday --> encontrado: " + encontrado);
                    // if(!encontrado){
                    // execute();
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Angulo Restante2: " + getRadarTurnRemaining());
                    // setTurnRadarRight(100); execute();
                    // turnLeft(getHeading());
                    // turnLeft(getHeading());
                    if (!encontrado) {
                        setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
                        execute();
                    }
                    estado = 3;


                    break;

                case 3: // dirigirse enemigo
                    System.out.println("Wednesday");
                    //execute();
                    //setAhead(enemigo_cercano.distancia - 80);
                    //execute();
                    //estado = 4;
                    break;
                case 4: // diparar y orbitar
                    System.out.println("Thursday");

                    estado = 5;
                    break;
                case 5: // 
                    System.out.println("Friday");

                    break;
                case 6:
                    System.out.println("Saturday");

                    break;

            }
            // if(case == 2) break;
            System.out.println("@@@@@@@@ ITERACION BUCLE");
            System.out.println("@@@@@@@@ FIN BUCLE  -- estado: " + estado);
        //}

        

    }

    @Override
    public void waitFor(Condition condition) {
        Condition waitCondition = condition;
        do {
            execute(); // Always tick at least once
        } while (!condition.test());
        waitCondition = null;
    }

    public void localizar_enemigo() {
        System.out.println("@@@@@@@@ DENTRO DE : localizar_enemigo() & encontrado: " + encontrado);
        double r_heading = e_bearing, g_heading = getGunHeading();
        System.out.println("@@@@antes@@@@ e_bearing: " + e_bearing + "    getGunHeading(): " + getGunHeading());
        // if(encontrado){
        double grados_girados;
        if (g_heading <= 270) {
            if (r_heading > g_heading) {
                grados_girados = r_heading - g_heading;
                setTurnGunRight(grados_girados);
            } else {
                grados_girados = g_heading - r_heading;
                setTurnGunLeft(grados_girados);
            }

        } else { // headin > 270
            if (r_heading > 270) {
                if (r_heading > g_heading) {
                    grados_girados = r_heading - g_heading;
                    setTurnGunRight(grados_girados);
                } else {
                    grados_girados = g_heading - r_heading;
                    setTurnGunLeft(grados_girados);
                }
            } else {
                grados_girados = g_heading - r_heading;
                setTurnGunLeft(grados_girados);
            }
        }

        execute();

        System.out.println("@@@despues@@@@@ e_bearing: " + e_bearing + "    getGunHeading(): " + getGunHeading()
                + "  ==> grados_girados: " + grados_girados);
        if (getGunHeading() != r_heading) {
            System.out.println("Dentro: g_heading != r_heading");
            localizar_enemigo();
        }

        setFire(2);
        execute();
        setFire(2);
        execute();
        setFire(2);
        execute();
        // }
    }

    public void copia(LinkedList<node> l1, LinkedList<node> l2) { // li ==> tx l2==> enemigos
        System.out.println("en la funcion de copia(2linkedlist)");
        print(l1);
        System.out.println("");
        if (l1.isEmpty()) {
            for (int i = 0; i < l2.size(); i++) {
                l1.add(l2.get(i));
                System.out.println("11 A??ADIENDO --> " + l2.get(i).name);
            }
            Collections.sort(l1);
        } else {

            System.out.println("22 REMOVING ... --> ");
            l1.clear();

            print(l1);
            System.out.println("");
            for (int i = 0; i < l2.size(); i++) {
                l1.add(l2.get(i));
                System.out.println("22 A??ADIENDO --> " + l2.get(i).name);
            }
            print(l1);
            System.out.println("");
            Collections.sort(l1);
        }

    }

    public void enviar_distancias() {
        // System.out.println("######### dentro de la funcion enviar_distancias() ");

        String quien_soy = getName();
        switch (quien_soy) {
            case "epsevg.upc.edu.c2223.ALDIRobot* (1)" -> copia(t1, enemigos);
            case "epsevg.upc.edu.c2223.ALDIRobot* (2)" -> copia(t2, enemigos);
            case "epsevg.upc.edu.c2223.ALDIRobot* (3)" -> copia(t3, enemigos);
            case "epsevg.upc.edu.c2223.ALDIRobot* (4)" -> copia(t4, enemigos);
            case "epsevg.upc.edu.c2223.ALDIRobot* (5)" -> copia(t5, enemigos);
            default -> {
            }
        }

        for (int i = 0; i < enemigos.size(); i++) {
            try {
                broadcastMessage("enemigoCercano:" + enemigos.get(i).distancia + "," + enemigos.get(i).name);
            } catch (IOException ignored) {
            }
        }
        System.out.println("######### IMPRIMIENDO T1: ");
        print(t1);
        System.out.println("");
        System.out.println("######### IMPRIMIENDO T2: ");
        print(t2);
        System.out.println("");
        System.out.println("######### IMPRIMIENDO T3: ");
        print(t3);
        System.out.println("");
        System.out.println("######### IMPRIMIENDO T4: ");
        print(t4);
        System.out.println("");
        System.out.println("######### IMPRIMIENDO T5: ");
        print(t5);
        System.out.println("");

    }

    public void a??adir_node(LinkedList<node> l, node n) {
        if (l.size() != 5) { // si no esta completa
            l.add(n);
            Collections.sort(l);
        } else { // hacer update

            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).name.equals(n.name)) {
                    if (l.get(i).distancia != n.distancia) { // si la distancia es diferente, hacer UPDATE distancia
                        l.get(i).distancia = n.distancia;
                        Collections.sort(l);
                    }
                    i = l.size();
                }
            }
        }

        System.out.println("22######### IMPRIMIENDO T1: ");
        print(t1);
        System.out.println("");
        System.out.println("22######### IMPRIMIENDO T2: ");
        print(t2);
        System.out.println("");
        System.out.println("22######### IMPRIMIENDO T3: ");
        print(t3);
        System.out.println("");
        System.out.println("22######### IMPRIMIENDO T4: ");
        print(t4);
        System.out.println("");
        System.out.println("22######### IMPRIMIENDO T5: ");
        print(t5);
        System.out.println("");

        if (t1.size() == 5 && t2.size() == 5 && t3.size() == 5 && t4.size() == 5 && t5.size() == 5) {
            System.out.println("22#########22#########22#########22######### llenos = true");
            llenos = true;
            estado = 1;
            execute();
            // getRobotRunnable(); //This method is called by the game to invoke the run()
            // method of your robot, where the program of your robot is implemented.
            run();
        }
    }

    public node buscar(LinkedList<node> l) {
        node e_cercano = new node(0.0, "");
        for (int i = 0; i < l.size(); i++) {
            boolean trobat = false;
            for (int j = 0; j < list_enemigos.size(); j++) {
                if (l.get(i).name.equals(list_enemigos.get(j)))
                    trobat = true;
            }
            if (!trobat) {

                e_cercano = l.get(i);
                i = l.size();
            }
        }
        return e_cercano;
    }

    public void asignarEnemigo() {
        // System.out.println("()()()()() ESTOY EN --> asignarEnemigo()");
        // System.out.println("()()()()() ESTOY EN --> asignarEnemigo() --> llenos:" +
        // llenos);
        // System.out.println("()()()()() ESTOY EN --> asignarEnemigo() -->
        // enemigo_cercano:" + enemigo_cercano);

        String quien_soy = getName();

        switch (quien_soy) {
            case "epsevg.upc.edu.c2223.ALDIRobot* (1)": // t1
                enemigo_cercano = t1.get(0);
                list_enemigos.add(enemigo_cercano.name);
                break;
            case "epsevg.upc.edu.c2223.ALDIRobot* (2)": // t1 --> t2
                list_enemigos.add(t1.get(0).name);
                enemigo_cercano = buscar(t2);
                list_enemigos.add(enemigo_cercano.name);
                break;
            case "epsevg.upc.edu.c2223.ALDIRobot* (3)":
                list_enemigos.add(t1.get(0).name);
                list_enemigos.add(buscar(t2).name);

                enemigo_cercano = buscar(t3);
                list_enemigos.add(enemigo_cercano.name);
                break;
            case "epsevg.upc.edu.c2223.ALDIRobot* (4)":
                list_enemigos.add(t1.get(0).name);
                list_enemigos.add(buscar(t2).name);
                list_enemigos.add(buscar(t3).name);

                enemigo_cercano = buscar(t4);
                list_enemigos.add(enemigo_cercano.name);
                break;
            case "epsevg.upc.edu.c2223.ALDIRobot* (5)":
                list_enemigos.add(t1.get(0).name);
                list_enemigos.add(buscar(t2).name);
                list_enemigos.add(buscar(t3).name);
                list_enemigos.add(buscar(t4).name);

                enemigo_cercano = buscar(t5);
                list_enemigos.add(enemigo_cercano.name);
                break;
            default: {
            }
        }

        System.out.println("<<<<<<<<<<<<<<<<<<<<Imprimiendo list_enemigos>>>>>>>>>>>>>>>>>>>>>>");
        for (int i = 0; i < list_enemigos.size(); i++) {
            System.out.print(" - " + list_enemigos.get(i));
        }
        System.out.println("");
        System.out.println("<<<<<<<<<<<<<<<<<<<<-------------- enemigo_cercano = " + enemigo_cercano.name
                + " --------------->>>>>>>>>>>>>>>>>>>>>>");

        System.out.println("--------------------------HE ACABADO LA FUNCION---------------------------");

        if (enemigo_cercano != null) {
            estado = 2;
            estado_scanned = 2;
            run();
        }

    }


    public double obtenirDistanciaEsquina(double xc, double xtank, double yc, double ytank) {
        return Math.sqrt((Math.pow((xc - xtank), 2)) + (Math.pow((yc - ytank), 2)));
    }

    public void remove(LinkedList<node> a, String name) {
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).name.equals(name)) {
                a.remove(i);
                i = a.size();
            }
        }

    }

    public void enviarMensajesAsignaci??n() {
        try {
            sendMessage(distArray.get(0).name, "DistanciaHaciaLaEsquina:" + distArray.get(0).distancia + "-" + 0);
        } catch (IOException ignored) {
        }

        try {
            sendMessage(distArray.get(1).name, "DistanciaHaciaLaEsquina:" + distArray.get(1).distancia + "-" + 1);
        } catch (IOException ignored) {
        }

        try {
            sendMessage(distArray.get(2).name, "DistanciaHaciaLaEsquina:" + distArray.get(2).distancia + "-" + 2);
        } catch (IOException ignored) {
        }
        try {
            sendMessage(distArray.get(3).name, "DistanciaHaciaLaEsquina:" + distArray.get(3).distancia + "-" + 3);
        } catch (IOException ignored) {
        }

    }

  
    public void onMessageReceived(MessageEvent event) {
        out.println(event.getSender() + " sent me: " + event.getMessage()); // + " soy: "+event.getMessage().getClass()
        String mssg = (event.getMessage()).toString();
        String sender = (event.getSender());
        String[] ssb1 = mssg.split(":");
        String command = ssb1[0];

        System.out.println("Quien me envia:" + sender);

        if (command.equals("enemigoCercano")) {
            String segunda_parte = ssb1[1];
            String[] ssb2 = segunda_parte.split(",");
            // System.out.println("@@@@@@@@@@@@@@@@@@@ segunda_parte : "+ssb2[0] + " & "+
            // ssb2[1]);
            EnemigoRecibido = new node(Double.parseDouble(ssb2[0]), ssb2[1]);

            switch (sender) {
                case "epsevg.upc.edu.c2223.ALDIRobot* (1)" -> a??adir_node(t1, EnemigoRecibido);
                case "epsevg.upc.edu.c2223.ALDIRobot* (2)" -> a??adir_node(t2, EnemigoRecibido);
                case "epsevg.upc.edu.c2223.ALDIRobot* (3)" -> a??adir_node(t3, EnemigoRecibido);
                case "epsevg.upc.edu.c2223.ALDIRobot* (4)" -> a??adir_node(t4, EnemigoRecibido);
                case "epsevg.upc.edu.c2223.ALDIRobot* (5)" -> a??adir_node(t5, EnemigoRecibido);
                default -> {
                }
            }
            // EnemigoRecibido = ssb1[1];
            // if()a??adir_node(t1, EnemigoRecibido)
            // System.out.println("--------------------------IMPRIMIENDO ENEMIGO MAS
            // CERCANO---------------------------");
            // System.out.println("EnemigoMasCercano: "+enemigos[0].name);
            // System.out.println("-------------------------- ACABO IMPRIMIENDO ENEMIGO MAS
            // CERCANO---------------------------");

        }

        // if(command.equals("EnvioCoords")){
        // String comvalue = ssb1[1];
        // String[] ssb2 = comvalue.split(",");
        // String coordX = ssb2[0];
        // String coordY = ssb2[1];
        // X_TankPos = Double.parseDouble(coordX);
        // Y_TankPos = Double.parseDouble(coordY);
        // calculaDistancia(sender, X_TankPos, Y_TankPos);

        // }else if(command.equals("DistanciaHaciaLaEsquina")){
        // String s = ssb1[1];
        // String[] ssb2 = s.split("-");
        // String stringdist = ssb2[0];
        // System.out.println("Distancia llegada:"+stringdist);
        // double dist = Double.parseDouble(stringdist);
        // String strgindice = ssb2[1];
        // cantonada_asignada = Integer.parseInt(strgindice);

        // double ang = calcularAngulo(getX(), getY(), cantonada_asignada);

        // girar(ang);
        // ahead(dist);

        // he_llegado = true;

        // centinella(cantonada_asignada);

        // }
        // else if(command.equals("kamikaze")){
        // kamikaze = ssb1[1];

        // }

    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        System.out.println(">> ESTOY DETECTANDO AL ROBOT: " + e.getName());
        String name = e.getName();
        switch (estado_scanned) {
            case 0:
                if (!isTeammate(name)) {
                    double distancia_enemigo = e.getDistance();

                    System.out.println("BEaring: " + e.getBearing() + " Del robot: " + e.getName());
                    System.out.println("ESTOY A ESTA DISTANCIA " + distancia_enemigo + "DEL ENEMIGO: " + name);
                    node n = new node(distancia_enemigo, name);

                    boolean find = false;
                    int i = 0;
                    while (!find && i < enemigos.size()) {
                        if (enemigos.get(i).name.equals(name)) {
                            if (enemigos.get(i).distancia != distancia_enemigo) { // si la distancia es diferente, hacer UPDATE distancia
                                enemigos.get(i).distancia = distancia_enemigo;
                                Collections.sort(enemigos);
                            }
                            find = true;
                        }
                        i++;
                    }

                    if (!find) {
                        enemigos.add(n);
                        Collections.sort(enemigos);
                    }
                }

                if (enemigos.size() == 5)
                    enviar_distancias();

                break;
            case 2:
                if ((enemigo_cercano.name).equals(e.getName())) {

                    encontrado = true;
                    System.out.println("==========================================");
                    double AbsAngleToEnemy = getHeadingRadians() + e.getBearingRadians();

                    double radarTurn = Utils.normalRelativeAngle(AbsAngleToEnemy - getRadarHeadingRadians());
                    double turretTurn = Utils.normalRelativeAngle(AbsAngleToEnemy - getGunHeadingRadians());
                    double bodyTurn = Utils.normalRelativeAngle(AbsAngleToEnemy - getHeadingRadians()); // NEW

                    System.out.println("RADAR_TURN_RATE_RADIANS: " + Rules.RADAR_TURN_RATE_RADIANS);

                    double extraTurn = Math.min(Math.atan(45.0 / e.getDistance()), Rules.RADAR_TURN_RATE_RADIANS); 
                    radarTurn += (radarTurn < 0 ? -extraTurn : extraTurn);

                    System.out.println("radarTurn: " + radarTurn);
                    
                    setTurnRadarRightRadians(radarTurn);
                    setAdjustRadarForGunTurn(true);
                    setTurnGunRightRadians(turretTurn);
                    
                    double get_gun = getGunHeadingRadians(), get_body = getHeadingRadians();
                    if(get_gun > get_body)setTurnRightRadians(get_gun-get_body); //gira entero
                    else setTurnLeftRadians(get_body-get_gun);
                    
                    setTurnGunRightRadians(turretTurn);
                    
                    System.out.println("IM LOOKING : " + Math.toDegrees(getHeadingRadians()) +"  --> bodyTurn: "+bodyTurn);
                    System.out.println("RADAR IL LOOKING : " + Math.toDegrees(getRadarHeadingRadians()));
                    System.out.println("GUN IL LOOKING : " + Math.toDegrees(getGunHeadingRadians()));
                 

                    System.out.println("--------------------------------------------");
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!! ENCONTRADOOO !!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println("--------------------------------------------");
                
                    
                    estado = 3;
                    estado_scanned=3;
                    run();
                    
                } else {
                    encontrado = false;
                }
                break;
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        setTurnLeft(180);
        setAhead(100);
        execute();
        estado = 2;
    }
}
