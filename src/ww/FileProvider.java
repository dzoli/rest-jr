package ww;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import ww.beans.Bean;

@Path("api")
public class FileProvider {
	private final String REPORT_LOCATION = "../report.jrxml";

//	@Path("/Integracao")
//	public class Integracao {
//
	@Context
	private HttpServletRequest httpServletRequest;
//
//	
//	public class PdfGenerator {
//
//		public byte[]  generateJasperReportPDF(HttpServletRequest httpServletRequest, String jasperReportName, ByteArrayOutputStream outputStream, Map parametros) {
//		    JRPdfExporter exporter = new JRPdfExporter();
//		    try {
//		        String reportLocation = httpServletRequest.getRealPath("/") +"resources\\jasper\\" + jasperReportName + ".jrxml";
//
//		        InputStream jrxmlInput = new FileInputStream(new File(reportLocation)); 
//		        //this.getClass().getClassLoader().getResource("data.jrxml").openStream();
//		        JasperDesign design = JRXmlLoader.load(jrxmlInput);
//		        JasperReport jasperReport = JasperCompileManager.compileReport(design);
//		        //System.out.println("Report compiled");
//
//		        //JasperReport jasperReport = JasperCompileManager.compileReport(reportLocation);
//		        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, HibernateUtils.currentSession().connection()); // datasource Service
//
//		        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);   
//		        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
//		        exporter.exportReport();
//		    } catch (Exception e) {
//		        e.printStackTrace();
//		        System.out.println("Error in generate Report..."+e);
//		    } finally {
//		    }
//		    return outputStream.toByteArray();
//		}
//		}
//	@GET
//	@Path("/gerarPdf")
//	public Response geraPDF(@QueryParam("relatorio") String arquivoJrxml,
//	                        @QueryParam("autorizacao") String autorizacao){
//
//	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//	    Map fillParams = new HashMap(); 
//	    fillParams.put("IMPRAUTORIZACAO", autorizacao);
//	    PdfGenerator pdf = new PdfGenerator();
//	    byte[] bytes= pdf.generateJasperReportPDF(httpServletRequest, arquivoJrxml, outputStream, fillParams);
//
//	    String nomeRelatorio= arquivoJrxml + ".pdf";
//	return Response.ok(bytes).type("application/pdf").header("Content-Disposition","filename=\""+nomeRelatorio+"\"").build();
//	}

	private class PdfGenerator {
		 Bean[] beans = { new Bean("ime1"), new Bean("ime2"), new Bean("ime3") };
		 JRPdfExporter exporter = new JRPdfExporter();

		@SuppressWarnings("rawtypes")
		public  byte[] generateJasperReportPDF(ByteArrayOutputStream outputStream) throws JRException, FileNotFoundException {
//			InputStream is = FileProvider.class.getResourceAsStream("../Reports/report1.jrxml");
			InputStream resource = httpServletRequest.getServletContext().getResourceAsStream("/WEB-INF/report1.jrxml");
//			String path = "res/report1.jrxml";
//			InputStream is = new FileInputStream(new File(path));
			JasperDesign design = JRXmlLoader.load(resource);
			JasperReport jReport = JasperCompileManager.compileReport(design);
			System.out.println("report compailed...");

			// create data for report
			Map someParameters = null;
			JRBeanArrayDataSource dataSource = new JRBeanArrayDataSource(beans);

			JasperPrint jPrint = JasperFillManager.fillReport(jReport, someParameters, dataSource);

//			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jPrint);
//			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.setExporterInput(new SimpleExporterInput(jPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			return outputStream.toByteArray();
		}

	}

	@GET
	@Path("voj")
	public Response jsonREST() {
		try {
			PdfGenerator pdfg = new PdfGenerator();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte [] bytes = pdfg.generateJasperReportPDF(outputStream);
			String fileName = "izvestaj.pdf";
			return Response.ok(bytes).type("application/pdf").header("Content-Disposition", "filename=\"" + fileName + "\"").build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(404).build();
		}
	}

}
