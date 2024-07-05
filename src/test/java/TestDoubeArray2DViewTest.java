/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.rappsilber.utils.DoubleArray2DView;

/**
 *
 * @author Lutz Fischer <lfischer@staffmail.ed.ac.uk>
 */
public class TestDoubeArray2DViewTest {
    
    public TestDoubeArray2DViewTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testSort() {
        double[] testdata = new double[]{100,1,99,2,98,3,97,4,96,5};
        double[] sortedcol0 = new double[]{96,5,97,4,98,3,99,2,100,1};
        double[] sortedcol1 = new double[]{100,1,99,2,98,3,97,4,96,5};
        
        DoubleArray2DView view = new DoubleArray2DView(testdata, 2);
        
        view.quicksort(0);
        for (int i =0; i<testdata.length;i++) {
            assertEquals(testdata[i], sortedcol0[i]);
        }
        view.quicksort(1);
        for (int i =0; i<testdata.length;i++) {
            assertEquals(testdata[i], sortedcol1[i]);
        }

    }
}
