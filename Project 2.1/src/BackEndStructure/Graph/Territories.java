package BackEndStructure.Graph;

public class Territories {

    public Graph getGraph() {
        Graph g = new Graph();
        Territory[] territories = getTerritories();

        // Add all vertices
        Vertex v1 = new Vertex(territories[0]);
        Vertex v2 = new Vertex(territories[1]);
        Vertex v3 = new Vertex(territories[2]);
        Vertex v4 = new Vertex(territories[3]);
        Vertex v5 = new Vertex(territories[4]);
        Vertex v6 = new Vertex(territories[5]);
        Vertex v7 = new Vertex(territories[6]);
        Vertex v8 = new Vertex(territories[7]);
        Vertex v9 = new Vertex(territories[8]);
        Vertex v10 = new Vertex(territories[9]);
        Vertex v11 = new Vertex(territories[10]);
        Vertex v12 = new Vertex(territories[11]);
        Vertex v13 = new Vertex(territories[12]);
        Vertex v14 = new Vertex(territories[13]);
        Vertex v15 = new Vertex(territories[14]);
        Vertex v16 = new Vertex(territories[15]);
        Vertex v17 = new Vertex(territories[16]);
        Vertex v18 = new Vertex(territories[17]);
        Vertex v19 = new Vertex(territories[18]);
        Vertex v20 = new Vertex(territories[19]);
        Vertex v21 = new Vertex(territories[20]);
        Vertex v22 = new Vertex(territories[21]);
        Vertex v23 = new Vertex(territories[22]);
        Vertex v24 = new Vertex(territories[23]);
        Vertex v25 = new Vertex(territories[24]);
        Vertex v26 = new Vertex(territories[25]);
        Vertex v27 = new Vertex(territories[26]);
        Vertex v28 = new Vertex(territories[27]);
        Vertex v29 = new Vertex(territories[28]);
        Vertex v30 = new Vertex(territories[29]);
        Vertex v31 = new Vertex(territories[30]);
        Vertex v32 = new Vertex(territories[31]);
        Vertex v33 = new Vertex(territories[32]);
        Vertex v34 = new Vertex(territories[33]);
        Vertex v35 = new Vertex(territories[34]);
        Vertex v36 = new Vertex(territories[35]);
        Vertex v37 = new Vertex(territories[36]);
        Vertex v38 = new Vertex(territories[37]);
        Vertex v39 = new Vertex(territories[38]);
        Vertex v40 = new Vertex(territories[39]);
        Vertex v41 = new Vertex(territories[40]);
        Vertex v42 = new Vertex(territories[41]);

        // Add vertices to graph
        g.AddVertex(v1);
        g.AddVertex(v2);
        g.AddVertex(v3);
        g.AddVertex(v4);
        g.AddVertex(v5);
        g.AddVertex(v6);
        g.AddVertex(v7);
        g.AddVertex(v8);
        g.AddVertex(v9);
        g.AddVertex(v10);
        g.AddVertex(v11);
        g.AddVertex(v12);
        g.AddVertex(v13);
        g.AddVertex(v14);
        g.AddVertex(v15);
        g.AddVertex(v16);
        g.AddVertex(v17);
        g.AddVertex(v18);
        g.AddVertex(v19);
        g.AddVertex(v20);
        g.AddVertex(v21);
        g.AddVertex(v22);
        g.AddVertex(v23);
        g.AddVertex(v24);
        g.AddVertex(v25);
        g.AddVertex(v26);
        g.AddVertex(v27);
        g.AddVertex(v28);
        g.AddVertex(v29);
        g.AddVertex(v30);
        g.AddVertex(v31);
        g.AddVertex(v32);
        g.AddVertex(v33);
        g.AddVertex(v34);
        g.AddVertex(v35);
        g.AddVertex(v36);
        g.AddVertex(v37);
        g.AddVertex(v38);
        g.AddVertex(v39);
        g.AddVertex(v40);
        g.AddVertex(v41);
        g.AddVertex(v42);

        // Example Afghanistan(v1) is adjacent to ukraine(v36), ural(v37), china(v7), india(v16) , middle east
        // You dont need to return an edge (v1, v2) = (v2, v1)
        g.addEdge(v1, v36);
        g.addEdge(v1, v37);
        g.addEdge(v1, v7);
        g.addEdge(v1, v16);
        g.addEdge(v1, v22);

        g.addEdge(v2, v3);
        g.addEdge(v2, v26);
        g.addEdge(v2, v20);

        g.addEdge(v3, v26);
        g.addEdge(v3, v28);
        g.addEdge(v3, v41);

        g.addEdge(v4, v5);
        g.addEdge(v4, v29);

        g.addEdge(v5, v25);
        g.addEdge(v5, v29);
        g.addEdge(v5, v38);

        g.addEdge(v6, v11);
        g.addEdge(v6, v38);
        g.addEdge(v6, v41);

        g.addEdge(v7, v16);
        g.addEdge(v7, v23);
        g.addEdge(v7, v32);
        g.addEdge(v7, v33);
        g.addEdge(v7, v37);

        g.addEdge(v8, v9);
        g.addEdge(v8, v25);
        g.addEdge(v8, v34);

        g.addEdge(v9, v12);
        g.addEdge(v9, v21);
        g.addEdge(v9, v22);
        g.addEdge(v9, v25);
        g.addEdge(v9, v34);

        g.addEdge(v10, v24);
        g.addEdge(v10, v39);

        g.addEdge(v11, v28);
        g.addEdge(v11, v30);
        g.addEdge(v11, v41);

        g.addEdge(v12, v22);
        g.addEdge(v12, v25);
        g.addEdge(v12, v35);

        g.addEdge(v13, v15);
        g.addEdge(v13, v27);
        g.addEdge(v13, v31);
        g.addEdge(v13, v40);

        g.addEdge(v14, v15);
        g.addEdge(v14, v26);
        g.addEdge(v14, v28);
        g.addEdge(v14, v30);

        g.addEdge(v15, v31);

        g.addEdge(v16, v22);
        g.addEdge(v16, v32);

        g.addEdge(v17, v24);
        g.addEdge(v17, v32);
        g.addEdge(v17, v39);

        g.addEdge(v18, v20);
        g.addEdge(v18, v23);
        g.addEdge(v18, v33);
        g.addEdge(v18, v42);

        g.addEdge(v19, v20);
        g.addEdge(v19, v23);

        g.addEdge(v20, v23);
        g.addEdge(v20, v42);

        g.addEdge(v21, v34);

        g.addEdge(v22, v35);
        g.addEdge(v22, v36);

        g.addEdge(v23, v33);

        g.addEdge(v24, v39);

        g.addEdge(v25, v35);
        g.addEdge(v25, v40);

        g.addEdge(v26, v28);

        g.addEdge(v27, v31);
        g.addEdge(v27, v35);
        g.addEdge(v27, v36);
        g.addEdge(v27, v40);

        g.addEdge(v28, v30);
        g.addEdge(v28, v41);

        g.addEdge(v29, v38);

        g.addEdge(v31, v36);

        g.addEdge(v33, v37);
        g.addEdge(v33, v42);

        g.addEdge(v35, v36);
        g.addEdge(v35, v40);

        g.addEdge(v36, v37);

        // Print all vertices with their respective edges
        g.printGraph();

        return g;
    }

    public Territory[] getTerritories() {
        // Territories sorted on alphabet
        Territory t1 = new Territory("Afghanistan");
        Territory t2 = new Territory("Alaska");
        Territory t3 = new Territory("Alberta");
        Territory t4 = new Territory("Argentina");
        Territory t5 = new Territory("Brazil");
        Territory t6 = new Territory("Central America");
        Territory t7 = new Territory("China");
        Territory t8 = new Territory("Congo");
        Territory t9 = new Territory("East Africa");
        Territory t10 = new Territory("Eastern Australia");
        Territory t11 = new Territory("Eastern United States");
        Territory t12 = new Territory("Egypt");
        Territory t13 = new Territory("Great Britain");
        Territory t14 = new Territory("Greenland");
        Territory t15 = new Territory("Iceland");
        Territory t16 = new Territory("India");
        Territory t17 = new Territory("Indonesia");
        Territory t18 = new Territory("Irkutsk");
        Territory t19 = new Territory("Japan");
        Territory t20 = new Territory("Kamchatka");
        Territory t21 = new Territory("Madagascar");
        Territory t22 = new Territory("Middle East");
        Territory t23 = new Territory("Mongolia");
        Territory t24 = new Territory("New Guinea");
        Territory t25 = new Territory("North Africa");
        Territory t26 = new Territory("Northwest Territory");
        Territory t27 = new Territory("Northern Europe");
        Territory t28 = new Territory("Ontario");
        Territory t29 = new Territory("Peru");
        Territory t30 = new Territory("Quebec");
        Territory t31 = new Territory("Scandinavia");
        Territory t32 = new Territory("Siam");
        Territory t33 = new Territory("Siberia");
        Territory t34 = new Territory("South Africa");
        Territory t35 = new Territory("Southern Europe");
        Territory t36 = new Territory("Ukraine");
        Territory t37 = new Territory("Ural");
        Territory t38 = new Territory("Venezuela");
        Territory t39 = new Territory("Western Australia");
        Territory t40 = new Territory("Western Europe");
        Territory t41 = new Territory("Western United States");
        Territory t42 = new Territory("Yakutsk");

        return new Territory[] {t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19,t20,t21,t22,t23,t24,t25,t26,t27,t28,t29,t30,t31,t32,t33,t34,t35,t36,t37,t38,t39,t40,t41,t42};
    }
}
