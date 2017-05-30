package home;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.ejb.EJB;
import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;

import ejb.User;

/**
 *
 */
@SuppressWarnings("serial")
public class ChartBean implements Serializable {
	@EJB(mappedName="ejb/User")
	private User user;
	private long preReg;
	private long valDoc;
	private long creBan;
	private long assBan;
	private long valCli;
	private static final String KEY1 = "Preinscripción";
    public static final String KEY2 = "Validación Documentos";
    public static final String KEY3 = "Creación Producto Bancario";
    public static final String KEY4 = "Asociación Producto Bancario";
    public static final String KEY5 = "Validado";
	
	public long getPreReg() {
		return preReg;
	}

	public void setPreReg(long preReg) {
		this.preReg = preReg;
	}

	public long getValDoc() {
		return valDoc;
	}

	public void setValDoc(long valDoc) {
		this.valDoc = valDoc;
	}

	public long getCreBan() {
		return creBan;
	}

	public void setCreBan(long creBan) {
		this.creBan = creBan;
	}



	public long getAssBan() {
		return assBan;
	}

	public void setAssBan(long assBan) {
		this.assBan = assBan;
	}

	public void drawChart(OutputStream out, Object data) throws IOException {
		Long[] cont=user.getNumClientsDB();
		preReg=cont[0];
		valDoc=cont[1];
		creBan=cont[2];
		assBan=cont[3];
		valCli=cont[4];
	    DefaultPieDataset dataset = new DefaultPieDataset();
	    if(preReg!=0){
	    	dataset.setValue(KEY1, preReg);
	    }if(assBan!=0){
	    	dataset.setValue(KEY4, assBan);
	    }if(valDoc!=0){
	    	dataset.setValue(KEY2, valDoc);
	    }if(creBan!=0){
	    	dataset.setValue(KEY3, creBan);
	    }if(valCli!=0){
	    	dataset.setValue(KEY5, valCli);
	    }
	    JFreeChart chart = ChartFactory.createPieChart3D("Clientes", dataset, true, true, Locale.ENGLISH);
	    PiePlot3D plot = (PiePlot3D) chart.getPlot();
	    plot.setBackgroundPaint(Color.white);
        plot.setSectionPaint(KEY1, new Color(0x4ca939));
        plot.setSectionPaint(KEY2, Color.red);
        plot.setSectionPaint(KEY3, new Color(0x3968A9));
        plot.setSectionPaint(KEY4, Color.orange);
        plot.setSectionPaint(KEY5, new Color(0x8a2be2));
        /*plot.setExplodePercent(KEY1, 0.10);
        plot.setSimpleLabels(true);
        plot.setLabelOutlinePaint(null);
        plot.setLabelBackgroundPaint(null);
        plot.setLabelShadowPaint(null);
        plot.setLabelFont(new Font("Verdana",Font.BOLD,15));*/
        
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
            "{1} Cliente(s) ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
        plot.setLabelGenerator(gen);
	    BufferedImage bufferedImage = chart.createBufferedImage(600, 500);
	    ImageIO.write(bufferedImage, "gif", out);
	  }

	public void setValCli(long valCli) {
		this.valCli = valCli;
	}

	public long getValCli() {
		return valCli;
	}
	

}
