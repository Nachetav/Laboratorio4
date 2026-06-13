import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class DataGenerator {
    //clase que genera datos aleatorios :v

    private static final String[] categories = {
            "Sensor", "Motor", "Microcontrolador", "Cable", "Bateria", "Herramienta", "Modulo", "Kit"
    };
    private static final String[] locations = {
        "Estante_A", "Estante_B", "Caja_1", "Caja_2", "Laboratorio", "Bodega"
    };

    public static InventoryItem generateItem(int id) { //crear item
        String name = "Componente" + id;

        int roll = StdRandom.uniformInt(0,8);
        String category = categories[roll];

        roll = StdRandom.uniformInt(0,6);
        String location = locations[roll];

        int stockTotal = StdRandom.uniformInt(1,21);

        return new InventoryItem(id, name, category, location, stockTotal, stockTotal, 0);
    }

    public static ArrayList<InventoryOperation> generateOperations(int m, int keyUniverse, long seed) { //crear operaciones en una lista
        StdRandom.setSeed(seed);

        ArrayList<InventoryOperation> lista = new ArrayList<>(); //lista a retornar
        boolean[] idExiste = new boolean[keyUniverse + 1]; //"universo" de IDs

        for(int i=0;i<m;i++) {
            double roll = StdRandom.uniformDouble(); //probabilidad :p
            int idrandom = StdRandom.uniformInt(1, keyUniverse+1); //id generada

            if(roll < 0.35) { //purchase
                InventoryItem item = null;
                if(idExiste[idrandom] == false) { //si "no existe" se crea con ayuda de generateItem
                    item = generateItem(idrandom);
                    idExiste[idrandom] = true;
                }
                lista.add(new InventoryOperation(OperationType.PURCHASE, idrandom, StdRandom.uniformInt(1,6), item));
            }
            else if(roll < 0.65) { //query
                lista.add(new InventoryOperation(OperationType.QUERY, idrandom, 0, null));
            }
            else if(roll < 0.80) { //lend
                lista.add(new InventoryOperation(OperationType.LEND, idrandom, StdRandom.uniformInt(1,6), null));
            }
            else if(roll < 0.90) { //receive
                lista.add(new InventoryOperation(OperationType.RECEIVE, idrandom, StdRandom.uniformInt(1,6), null));
            }
            else { //dispose
                lista.add(new InventoryOperation(OperationType.DISPOSE, idrandom, 0, null));
                idExiste[idrandom] = false;
            }
        }
        return lista;
    }
}
