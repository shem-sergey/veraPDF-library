package org.verapdf.model.impl.axl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Maksim Bezrukov
 */
public class XMPMMHistoryTest {

	@Test
	public void test() throws URISyntaxException, XMPException, IOException {
		try (FileInputStream in = new FileInputStream(
				getSystemIndependentPath("/org/verapdf/model/impl/axl/xmpMM-History.xml"))) {
			VeraPDFMeta meta = VeraPDFMeta.parse(in);
			AXLMainXMPPackage pack = new AXLMainXMPPackage(meta, true,
					PDFAFlavour.PDFA_1_B);
			List<? extends Object> list = pack
					.getLinkedObjects(AXLMainXMPPackage.PROPERTIES);
			assertEquals(1, list.size());
			if (list.size() != 0) {
				Object obj = list.get(0);
				assertTrue(obj instanceof AXLXMPMMHistoryProperty);
				AXLXMPMMHistoryProperty historyProperty = (AXLXMPMMHistoryProperty) obj;
				assertTrue(historyProperty.getisValueTypeCorrect().booleanValue());
				assertTrue(historyProperty.getisPredefinedInXMP2004().booleanValue());
				assertTrue(historyProperty.getisPredefinedInXMP2005().booleanValue());
				assertFalse(historyProperty.getisDefinedInCurrentPackage().booleanValue());
				assertFalse(historyProperty.getisDefinedInMainPackage().booleanValue());

				List<? extends Object> resList = historyProperty
						.getLinkedObjects(AXLXMPMMHistoryProperty.RESOURCE_EVENTS);
				assertEquals(1, resList.size());
				if (resList.size() != 0) {
					Object object = resList.get(0);
					assertTrue(object instanceof AXLXMPMMHistoryResourceEvent);
					AXLXMPMMHistoryResourceEvent event = (AXLXMPMMHistoryResourceEvent) object;
					assertEquals("created", event.getaction());
					assertEquals("PDF file was created via veraPDF Test Builder", event.getparameters());
					assertNull(event.getwhen());
				}
			}
		}
	}

	private static String getSystemIndependentPath(String path)
			throws URISyntaxException {
		URL resourceUrl = ClassLoader.class.getResource(path);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return resourcePath.toString();
	}
}
