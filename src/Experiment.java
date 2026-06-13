//clase encargada de ejecutar los metodos de BST y RDBST para luego contar los tiempos
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StopwatchCPU;

import java.util.ArrayList;

public class Experiment {

    public static void main(String[] args) {
        int[] exponente = {12, 13, 14, 15, 16, 17, 18, 19}; //

        for (int t : exponente) { //ciclo que recorre la cantidad de m exponencial
            int m = (int) Math.pow(2, t); //cantidad de operaciones por ciclo
            int keyUniverse = 4 * m; //universo de claves

            Out out = new Out("inventory_experiment_" + m + ".csv");//archivo .csv

            // Encabezado
            out.println("instancia,estructura,m,purchase_total,query_total,lend_total,receive_total,dispose_total,query_successful,query_failed,lend_successful,lend_failed,receive_successful,receive_failed,final_size,final_height,elapsed_seconds");

            for (int i = 1; i <= 30; i++) { // 30 instancias de BST y RedBlackBST
                long seed = m + i;

                //generar la lista
                ArrayList<InventoryOperation> ops = DataGenerator.generateOperations(m, keyUniverse, seed);

                //crear los arboles
                BSTInventoryIndex bst = new BSTInventoryIndex();
                RedBlackBSTInventoryIndex rbbst = new RedBlackBSTInventoryIndex();

                //something...
                runAndMeasure(bst, "BST", ops, i, m, out);
                runAndMeasure(rbbst, "RedBlackBST", ops, i, m, out);

                //something else...
                validarCorrectitud(bst, rbbst, m, keyUniverse);
            }
            out.close();
            System.out.println("Experimento completado para m = " + m); //para saber si la pc murió o no xd
        }
    }

    private static void runAndMeasure(InventoryIndex index, String estructura, ArrayList<InventoryOperation> ops, int instancia, int m, Out out) {
        // contadores para registrarlos en el .csv
        int p_total = 0, q_total = 0, l_total = 0, r_total = 0, d_total = 0;
        int q_succ = 0, q_fail = 0, l_succ = 0, l_fail = 0, r_succ = 0, r_fail = 0;

        StopwatchCPU timer = new StopwatchCPU();

        for (InventoryOperation op : ops) {
            int id = op.getKey();
            int cantidad = op.getQuantity();

            switch (op.getType()) { //uso de switch para simplificar el uso de ENUMs
                case PURCHASE://comprar
                    p_total++;
                    InventoryItem existingItem = index.get(id);
                    if (existingItem == null) { //si no existe, put
                        index.put(id, op.getItem());
                    } else { //si existe, añadir cantidad y put
                        existingItem.addStock(id, cantidad);
                        index.put(id, existingItem);
                    }
                    break;

                case QUERY://consulta de productos
                    q_total++;
                    if (index.get(id) != null) q_succ++; else q_fail++;
                    break;

                case LEND://prestar
                    l_total++;
                    InventoryItem lendItem = index.get(id);
                    if (lendItem != null && lendItem.lend(id, cantidad)) {
                        l_succ++;
                    } else {
                        l_fail++;
                    }
                    break;

                case RECEIVE://devolver
                    r_total++;
                    InventoryItem recItem = index.get(id);
                    if (recItem != null && recItem.receive(id, cantidad)) {
                        r_succ++;
                    } else {
                        r_fail++;
                    }
                    break;

                case DISPOSE://eliminar
                    d_total++;
                    index.delete(id);
                    break;
            }
        }

        double elapsed = timer.elapsedTime();

        out.println(instancia + "," + estructura + "," + m + "," + p_total + "," + q_total + "," + l_total + "," + r_total + "," +
                d_total + "," + q_succ + "," + q_fail + "," + l_succ + "," + l_fail + "," + r_succ + "," + r_fail + "," +
                index.size() + "," + index.height() + "," + elapsed); // [cite: 384]
    }

    private static void validarCorrectitud(InventoryIndex bst, InventoryIndex rbbst, int m, int keyUniverse) {
        //validar que esan del mismo tamaño
        if (bst.size() != rbbst.size()) {
            throw new RuntimeException("TAMAÑOS DISTINTOS");
        }

        //validar que 100 claves aleatorias retornen el mismo resultado con get
        for (int j = 0; j < 100; j++) {
            int randomKey = StdRandom.uniformInt(1, keyUniverse + 1);
            InventoryItem item1 = bst.get(randomKey);
            InventoryItem item2 = rbbst.get(randomKey);

            if ((item1 == null && item2 != null) || (item1 != null && item2 == null)) {
                throw new RuntimeException("UN ARBOL TIENE UNA CLAVE Y EL OTRO NO");
            }
            if (item1 != null && item2 != null) {
                //validar que Available + OnLoan = Total en stock
                if (item1.getStockAvailable() + item1.getStockOnLoan() != item1.getStockTotal()) {
                    throw new RuntimeException("STOCK NO CUADRA");
                }
            }
        }
    }
}
