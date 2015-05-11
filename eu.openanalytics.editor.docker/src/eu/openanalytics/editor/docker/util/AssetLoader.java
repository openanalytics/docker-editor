package eu.openanalytics.editor.docker.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import eu.openanalytics.editor.docker.Activator;

public class AssetLoader {

	public static byte[] loadAsset(String path) throws IOException {
		URL url = FileLocator.find(Activator.getDefault().getBundle(), new Path(path), null);
		if (url == null) {
			return null;
		} else {
			InputStream input = null;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				input = url.openStream();
				byte[] buffer = new byte[4096];
				int len = 0;
				do {
					len = input.read(buffer, 0, buffer.length);
					if (len > 0) out.write(buffer, 0, len);
				} while (len >= 0);
			} finally {
				if (input != null) try { input.close(); } catch (IOException e) {}
			}
			return out.toByteArray();
		}
	}
}
