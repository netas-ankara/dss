/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 * 
 * This file is part of the "DSS - Digital Signature Services" project.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.esig.dss.model.x509;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.KeyUsageBit;
import eu.europa.esig.dss.enumerations.SignatureAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureValidity;
import eu.europa.esig.dss.model.DSSException;

public class CertificateTokenTest {

	private static final Logger LOG = LoggerFactory.getLogger(CertificateTokenTest.class);

	@Test
	public void test() throws Exception {
		String c1 = "MIID/TCCAuWgAwIBAgILBAAAAAABFWqxqn4wDQYJKoZIhvcNAQEFBQAwVzELMAkGA1UEBhMCQkUxGTAXBgNVBAoTEEdsb2JhbFNpZ24gbnYtc2ExEDAOBgNVBAsTB1Jvb3QgQ0ExGzAZBgNVBAMTEkdsb2JhbFNpZ24gUm9vdCBDQTAeFw0wNzEwMDQxMjAwMDBaFw0xNDAxMjYyMzAwMDBaMCgxCzAJBgNVBAYTAkJFMRkwFwYDVQQDExBCZWxnaXVtIFJvb3QgQ0EyMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxnNCHpL/dQ+Lv3SGpz/tshgtLZf5qfuYSiPf1Y3gjMYyHBYtB0LWLbZuL6f1/MaFgl2V3rUiAMyoU0Cfrwo1onrH4cr3YBBnDqdQcxdTlZ8inwxdb7ZBvIzr2h1GvaeUv/May9T7jQ4eM8iW1+yMU96THjQeilBxJli0XcKIidpg0okhP97XARg2buEscAMEZe+YBitdHmLcVWv+ZmQhX/gv4debKa9vzZ+qDEbRiMWdopWfrD8VrvJh3+/Da5oi2Cxx/Vgd7ACkOCCVWsfVN2O6T5uq/lZGLmPZCyPVivq1I/CJG6EUDSbaQfA4jzDtBSZ5wUtOobh+VVI6aUaEdQIDAQABo4H4MIH1MA4GA1UdDwEB/wQEAwIBBjAPBgNVHRMBAf8EBTADAQH/MB0GA1UdDgQWBBSFiuv0xbu+DlkDlN7WgAEV4xCcOTBDBgNVHSAEPDA6MDgGBWA4CQEBMC8wLQYIKwYBBQUHAgEWIWh0dHA6Ly9yZXBvc2l0b3J5LmVpZC5iZWxnaXVtLmJlIDA6BgNVHR8EMzAxMC+gLaArhilodHRwOi8vc2VjdXJlLmdsb2JhbHNpZ24ubmV0L2NybC9yb290LmNybDARBglghkgBhvhCAQEEBAMCAAcwHwYDVR0jBBgwFoAUYHtmGkUNl8qJUC99BM00qP/8/UswDQYJKoZIhvcNAQEFBQADggEBAH1t5NWhYEwrNe6NfOyI0orfIiEoy13BB5w214IoqfGSTivFMZBI2FQeBOquBXkoB253FXQq+mmZMlIl5qn0qprUQKQlicA2cSm0UgBe7SlIQkkxFusl1AgVdjk6oeNkHqxZs+J1SLy0NofzDA+F8BWy4AVSPujQ6x1GK70FdGmea/h9anxodOyPLAvWEckPFxavtvTuxwAjBTfdGB6Z6DvQBq0LtljcrLyojA9uwVDSvcwOTZK5lcTV54aE6KZWX2DapbDi2KY/oL6HfhOiDh+OPqa3YXzvCesY/h5v0RerHFFk49+ItSJryzwRcvYuzk1zYQL5ZykZc/PkVRV3HWE=";
		String c2 = "MIID7jCCAtagAwIBAgILBAAAAAABQaHhNLowDQYJKoZIhvcNAQEFBQAwOzEYMBYGA1UEChMPQ3liZXJ0cnVzdCwgSW5jMR8wHQYDVQQDExZDeWJlcnRydXN0IEdsb2JhbCBSb290MB4XDTEzMTAxMDExMDAwMFoXDTI1MDUxMjIyNTkwMFowKDELMAkGA1UEBhMCQkUxGTAXBgNVBAMTEEJlbGdpdW0gUm9vdCBDQTIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDGc0Iekv91D4u/dIanP+2yGC0tl/mp+5hKI9/VjeCMxjIcFi0HQtYttm4vp/X8xoWCXZXetSIAzKhTQJ+vCjWiesfhyvdgEGcOp1BzF1OVnyKfDF1vtkG8jOvaHUa9p5S/8xrL1PuNDh4zyJbX7IxT3pMeNB6KUHEmWLRdwoiJ2mDSiSE/3tcBGDZu4SxwAwRl75gGK10eYtxVa/5mZCFf+C/h15spr2/Nn6oMRtGIxZ2ilZ+sPxWu8mHf78NrmiLYLHH9WB3sAKQ4IJVax9U3Y7pPm6r+VkYuY9kLI9WK+rUj8IkboRQNJtpB8DiPMO0FJnnBS06huH5VUjppRoR1AgMBAAGjggEEMIIBADAOBgNVHQ8BAf8EBAMCAQYwEgYDVR0TAQH/BAgwBgEB/wIBATBQBgNVHSAESTBHMEUGCisGAQQBsT4BZAEwNzA1BggrBgEFBQcCARYpaHR0cDovL2N5YmVydHJ1c3Qub21uaXJvb3QuY29tL3JlcG9zaXRvcnkwHQYDVR0OBBYEFIWK6/TFu74OWQOU3taAARXjEJw5MDUGA1UdHwQuMCwwKqAooCaGJGh0dHA6Ly9jcmwub21uaXJvb3QuY29tL2N0Z2xvYmFsLmNybDARBglghkgBhvhCAQEEBAMCAAcwHwYDVR0jBBgwFoAUtgh7DXrMrCBMhlYyXs+rboUtcFcwDQYJKoZIhvcNAQEFBQADggEBALLLOUcpFHXrT8gK9htqXI8dV3LlSAooOqLkn+yRRxt/zS9Y0X0opocf56Kjdu+c2dgw6Ph3xE/ytMT5cu/60jT17BTk2MFkQhoAJbM/KIGmvu4ISDGdeobiBtSeiyzRb9JR6JSuuM3LvQp1n0fhsA5HlibT5rFrKi7Oi1luDbc4eAp09nPhAdcgUkRU9o/aAJLAJho3Zu9uSbw5yHW3PRGnmfSO67mwsnSDVswudPrZEkCnSHq/jwOBXAWCYVu5bru3rCdojd5qCTn/WyqbZdsgLAPR5Vmf/uG3d5HxTO1LLX1Zyp9iANuG32+nFusi89shA1GPDKWacEm0ASd8iaU=";
		String c3 = "MIIDjjCCAnagAwIBAgIIKv++n6Lw6YcwDQYJKoZIhvcNAQEFBQAwKDELMAkGA1UEBhMCQkUxGTAXBgNVBAMTEEJlbGdpdW0gUm9vdCBDQTIwHhcNMDcxMDA0MTAwMDAwWhcNMjExMjE1MDgwMDAwWjAoMQswCQYDVQQGEwJCRTEZMBcGA1UEAxMQQmVsZ2l1bSBSb290IENBMjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMZzQh6S/3UPi790hqc/7bIYLS2X+an7mEoj39WN4IzGMhwWLQdC1i22bi+n9fzGhYJdld61IgDMqFNAn68KNaJ6x+HK92AQZw6nUHMXU5WfIp8MXW+2QbyM69odRr2nlL/zGsvU+40OHjPIltfsjFPekx40HopQcSZYtF3CiInaYNKJIT/e1wEYNm7hLHADBGXvmAYrXR5i3FVr/mZkIV/4L+HXmymvb82fqgxG0YjFnaKVn6w/Fa7yYd/vw2uaItgscf1YHewApDgglVrH1Tdjuk+bqv5WRi5j2Qsj1Yr6tSPwiRuhFA0m2kHwOI8w7QUmecFLTqG4flVSOmlGhHUCAwEAAaOBuzCBuDAOBgNVHQ8BAf8EBAMCAQYwDwYDVR0TAQH/BAUwAwEB/zBCBgNVHSAEOzA5MDcGBWA4CQEBMC4wLAYIKwYBBQUHAgEWIGh0dHA6Ly9yZXBvc2l0b3J5LmVpZC5iZWxnaXVtLmJlMB0GA1UdDgQWBBSFiuv0xbu+DlkDlN7WgAEV4xCcOTARBglghkgBhvhCAQEEBAMCAAcwHwYDVR0jBBgwFoAUhYrr9MW7vg5ZA5Te1oABFeMQnDkwDQYJKoZIhvcNAQEFBQADggEBAFHYhd27V2/MoGy1oyCcUwnzSgEMdL8rs5qauhjyC4isHLMzr87lEwEnkoRYmhC598wUkmt0FoqW6FHvv/pKJaeJtmMrXZRY0c8RcrYeuTlBFk0pvDVTC9rejg7NqZV3JcqUWumyaa7YwBO+mPyWnIR/VRPmPIfjvCCkpDZoa01gZhz5v6yAlGYuuUGK02XThIAC71AdXkbc98m6tTR8KvPG2F9fVJ3bTc0R5/0UAoNmXsimABKgX77OFP67H6dh96tK8QYUn8pJQsKpvO2FsauBQeYNxUJpU4c5nUwfAA4+Bw11V0SoU7Q2dmSZ3G7rPUZuFF1eR1ONeE3gJ7uOhXY=";
		String c4 = "MIIFwzCCA6ugAwIBAgIUCn6m30tEntpqJIWe5rgV0xZ/u7EwDQYJKoZIhvcNAQELBQAwRjELMAkGA1UEBhMCTFUxFjAUBgNVBAoMDUx1eFRydXN0IFMuQS4xHzAdBgNVBAMMFkx1eFRydXN0IEdsb2JhbCBSb290IDIwHhcNMTUwMzA1MTMyMTU3WhcNMzUwMzA1MTMyMTU3WjBGMQswCQYDVQQGEwJMVTEWMBQGA1UECgwNTHV4VHJ1c3QgUy5BLjEfMB0GA1UEAwwWTHV4VHJ1c3QgR2xvYmFsIFJvb3QgMjCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBANeFl78RmOnwYoNMPIf5U2o3C/IPPIfOb9wmKb3FibrJgz337spbxm1Jc7TJRqMbNBM/wYlFV/TZsfs2ZUv7COJIcRHIbjuend+JZTemhfY7RBi2xjcwYkSSl2l9QjAk5A0MiWtj3sXh306pFGxT4GHO9hcvHTy95iJMHZP1EMShduxq3sVs35a0VkBCwGKSMKEtFZSg0iAGCW5qbeXrt77U8PEVfIvmTroTzEsnXpk8F12PgX8zPU/TPxvsXD/wPEx1bvKm1Z3aLQdjAsZy6ZS8TEmVT4hSyNvoaYL4zDRbIvCGp4m9SAptZoFtyMhk+wHh9OHe2Z7d21vUKpkmFRseTJIpgp7VkoGSQXAZ96Tlk0u8d2cx3Rz9MXANF5kM+Qw5GSoXtTBxVdUPrljhPS80m8+f9niFwpN6cj5mj5wWEWCPnolvZ77gR1o7DJpni89Gxq44o/KnvObWhWszJHAiS8sIm7vI+AIpHb4gDEa/a4ebsypmQjVGbKq6rfmYe+lQVRQxv7HaLe2ArWgk+2mr2HETMOZns4dA/Yl+8kPREd8vZS9kzl8UubG/Mb2HeFpZZYiq/FkySIbWTLkpS5XTdvN3JW1CHDiDTf2jX5t/Lax5Gw5CMZdjpPuKadUiDTSQMC6otOBttpSsvItO13D8xTiOZCXhTTmQzsmHhFhxAgMBAAGjgagwgaUwDwYDVR0TAQH/BAUwAwEB/zBCBgNVHSAEOzA5MDcGByuBKwEBAQowLDAqBggrBgEFBQcCARYeaHR0cHM6Ly9yZXBvc2l0b3J5Lmx1eHRydXN0Lmx1MA4GA1UdDwEB/wQEAwIBBjAfBgNVHSMEGDAWgBT/GCh2+UgFLKGu8SsbK7JT+Et8szAdBgNVHQ4EFgQU/xgodvlIBSyhrvErGyuyU/hLfLMwDQYJKoZIhvcNAQELBQADggIBAGoZFO1uecEsh9QNcH7X9njJCwROxLHOk3D+sFTAMs2ZMGQXvw/l4jP9BzZAcg4atmpZ1gDlaCDdLnINH2pkMSCEfUmmWjfrRcmF9dTHF5kH5ptV5AzoqbTOjFu1EVzPig4N1qx3gf4ynCSecs5U89BvolbW7MM3LGVYvlcAGvI1+ut7MV3CwRI9loGIlonBWVx65n9wNOeD4rHh4bhY79SV5GCc8JaXcozrhAIuZY+kt9J/Z93I055cqqmkoCUUBpvsT34tC38ddfEz2O3OuHVtPlu5mB0xDVbYQw8wkbIEa91WvpWAVWe+2M2D2RjuLg+GLZKecBPs3lHJQ3gCpU3I+V/EkVhGFndadKpAvAefMLmx9xIX3eP/JEAdemrRTxgKqpAd60Ae36EeRJIQmvKN4dFLRp7oRUKX6kWZ8+xm1QL68qZKJKrezrnK+T+Tb/mjuuqlPpmt/f97mfVl7vBZKGfXkJWkE4SphMHozs51k2MavDzq1WQfLSoSOcbDWjLtR5EWDrw4wVDej8oqkDQc7kGUnF4ZLvhFSZl0kbAEb+MEWrGrKqv+x9CWttrhSmQGbmBNvUJO/3jaJMobtNeWOWyu8Q6qp31IiyBMz2TWuJdGsE7RKlY6oJO9r4Ak4Ap+58rVyuiFVdw2KuGUaJPHZnJED4AhMmwlxyOAgwrr";
		
		CertificateToken certificate1 = getCertificate(c1);
		CertificateToken certificate2 = getCertificate(c2);
		CertificateToken certificate3 = getCertificate(c3);
		CertificateToken certificate4 = getCertificate(c4);
		assertNotNull(certificate1);
		assertNotNull(certificate2);
		assertNotNull(certificate3);
		assertNotNull(certificate4);

		assertNotNull(certificate1.getKeyUsageBits());
		assertNotNull(certificate1.getSignatureAlgorithm());
		assertNotNull(certificate1.getDSSId());
		assertNotNull(certificate1.getEntityKey());
		assertNotNull(certificate1.getDigest(DigestAlgorithm.SHA256));
		assertNotNull(certificate1.getDigest(DigestAlgorithm.SHA256));

		assertTrue(certificate1.checkKeyUsage(KeyUsageBit.CRL_SIGN));
		assertFalse(certificate1.checkKeyUsage(KeyUsageBit.NON_REPUDIATION));
		
		assertEquals(SignatureValidity.NOT_EVALUATED, certificate1.getSignatureValidity());
		assertEquals(SignatureValidity.NOT_EVALUATED, certificate2.getSignatureValidity());
		assertEquals(SignatureValidity.NOT_EVALUATED, certificate3.getSignatureValidity());
		assertEquals(SignatureValidity.NOT_EVALUATED, certificate4.getSignatureValidity());

		LOG.info("{}", certificate1);
		LOG.info("{}", certificate2);
		LOG.info("{}", certificate3);
		LOG.info("{}", certificate4);

		assertFalse(certificate1.isSelfIssued());
		assertTrue(certificate3.isSelfIssued());

		assertFalse(certificate1.isSignedBy(certificate3));
		assertEquals(SignatureValidity.INVALID, certificate1.getSignatureValidity());
		assertFalse(certificate1.isSelfSigned());
		assertTrue(certificate3.isSignedBy(certificate3));
		assertEquals(SignatureValidity.VALID, certificate3.getSignatureValidity());
		
		assertFalse(certificate3.isSignedBy(certificate4));
		assertEquals(SignatureValidity.INVALID, certificate3.getSignatureValidity());
		assertTrue(certificate3.isSelfSigned());
		assertEquals(SignatureValidity.VALID, certificate3.getSignatureValidity());
		assertTrue(certificate3.isSignedBy(certificate1));
		assertEquals(SignatureValidity.VALID, certificate3.getSignatureValidity());
		assertTrue(certificate3.isSignedBy(certificate2));
		assertEquals(SignatureValidity.VALID, certificate3.getSignatureValidity());
		
		assertNull(certificate3.getPublicKeyOfTheSigner());

		assertTrue(certificate1.equals(certificate1));
		assertFalse(certificate1.equals(certificate2));
		assertFalse(certificate1.equals(certificate3));
		assertFalse(certificate1.equals(certificate4));
		
		assertTrue(certificate1.isEquivalent(certificate1));
		assertTrue(certificate1.isEquivalent(certificate2));
		assertTrue(certificate1.isEquivalent(certificate3));
		assertFalse(certificate1.isEquivalent(certificate4));
		
		Date cert1NotBefore = certificate1.getNotBefore();
		assertTrue(certificate1.isValidOn(cert1NotBefore));
		assertFalse(certificate2.isValidOn(cert1NotBefore));
		assertTrue(certificate3.isValidOn(cert1NotBefore));
		assertFalse(certificate4.isValidOn(cert1NotBefore));
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cert1NotBefore);
		calendar.add(Calendar.SECOND, -1);
		assertFalse(certificate1.isValidOn(calendar.getTime()));
		
		Date cert1NotAfter = certificate1.getNotAfter();
		assertTrue(certificate1.isValidOn(cert1NotAfter));
		assertTrue(certificate2.isValidOn(cert1NotAfter));
		assertTrue(certificate3.isValidOn(cert1NotAfter));
		assertFalse(certificate4.isValidOn(cert1NotAfter));

		calendar.setTime(cert1NotAfter);
		calendar.add(Calendar.SECOND, 1);
		assertFalse(certificate1.isValidOn(calendar.getTime()));
	}

	@Test
	public void pss() throws IOException {
		Security.addProvider(new BouncyCastleProvider());
		File certFile = new File("src/test/resources/D-TRUST_CA_3-1_2016.cer");
		try (FileInputStream fis = new FileInputStream(certFile)) {
			CertificateToken certificate = getCertificate(fis);
			assertNotNull(certificate);
			assertEquals(SignatureAlgorithm.RSA_SSA_PSS_SHA512_MGF1, certificate.getSignatureAlgorithm());
		}
	}

	private CertificateToken getCertificate(String base64) {
		return getCertificate(new ByteArrayInputStream(Base64.getDecoder().decode(base64)));
	}

	private CertificateToken getCertificate(InputStream isOrigin) {
		try (InputStream is = isOrigin) {
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			return new CertificateToken((X509Certificate) factory.generateCertificate(is));
		} catch (Exception e) {
			throw new DSSException("Unable to read certificate", e);
		}
	}
}

