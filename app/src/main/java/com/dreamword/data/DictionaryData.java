package com.dreamword.data;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class DictionaryData {
    public static String getDictionaryJson() {
        byte[] compressed = Base64.decode("H4sIAC6ZG2oC/8y9aVMjWZom+r1/RUx9mEuYjWXnVrmMTbdZVlZv1ZXVbZXV3despj64JBfyxCVXuaQgybFrxiaQWAQEYpXYJXYkERCgBdB/GfNz3P1T/4V7HLIqs8aah3xUtOWYkSQRAQdfzvIuz/K//uLFi5+krIwd1n/y31/85OdGOG1YCc0e+rmW1l70/dJI/L8ZfSijJb5MRwzr5U/+W/D9aSNt3n+7027LpTU3n/Omz736mVefleeT/36TE405cbbvNPOidem2D/73cNE/WXWaZ/9+k38Yod/WInpKDfFb9acXL/7X/Wf190YkGPaDD9/7+INw6INPou9+HP3k00gkpIfC72vh+5+9/75UWuu/v4KH3/TdPyS0+MOV/eF3O80pMTHz3TcMWnbku1/8/V9+/896ot80UrFgCDtj6vYff/D+X8MxI6Gnvv3F7Z/88Z/+v//25GhJPRE2zEeH8xez7mmRGlG3tRS4QHm0467XqBHDtjZkJR4d0SvvsNcY0vofH253WpzNsw/x8Sf4epe+OssaeHQ8p7VPDZawHp8r8/uitk6NNqSW5OOXdrvtro9T49l65NHh3Paul39DDddv6+BVuJ0uO+CQbprW4OMvtzOuRnz4zN56yMzoj7+a7oE/NsIPqYUHwMUusLcfsq3Bx5+nXHutxqQGHIwZ6cfv2l27Za/QsrVE/+Mjyi3u+v7pHx9/JdVbaqh4Jv7oWL+Vldnf9f1VOGYPpbREOqarb37pzU15U+fU74hq4cfv3Rtvchu39vjS9oYvZPktN9wQeNHlG3ejRO5jqcfH82863LuxMunY4296bo8aTbMff9Pe5Cg1VkxLPL4jyvw0N5iuPT6YqFySp1Jk6PGtv82FHaYODuBsl1sElpV+fLCrC2qwVDhmWY/HQyqok9s71IiRDNqTm2d0sGE8/ujcmWPunaI1L2cq3I1a4MKmV7jNw9STaimA93q+Q674xIA+BB7cJfseQoYdAe91ixosbfSDmNlbK3BTRG0i2uP3OjHlzpxQA35jgShy6tDNH4nSGbdoM4nE469DlvPe1R4d/thov/M3dsVMlhrwq4wBzld5xYUqej/Y80rczh43TJQicHveoJZGCVuDOyfC2gA4qKs33DQJ/v/oYOfcYDZ6m+4hF0JYCRR1zjvNYW4DGLTA4TrDjRVTadDjozXz5PEKUj5RKpHv8xW4sjaXJ6eMrx+/sCy3H6X0VyBzdJpj3FI3+mNpcG0nTpM7qBMGmG5Oa4ObH+BOxewImyGq9AVMkOy4uOHOwqSpoRyxtPqnJ/W3X/3uL743/P9RuAu/95H20w/efzeqffjxT3/6wbuRT3/6yQfhDz7srXA33WPh7l8ezy296XNR4tLLz7WEBg55MbUtp7uickAN+i9ffvb4g78rqGvsC/6XOxIb62I+59ysqL96yV23GlEDC+2MfRCp2OOT5bdOsyO3b34nqi1uzHQmooPoU00Rb5iLJZKZpAHzCre4RSZ3jz/FzrJbvCb3BC2M1rFcWhPNHJkwgni7eC3OjrgsGyTG4jV3QvYbNngX1Qv22hL64zVCudxgA9mobeioDFCeFnNcvJi2IhrI3jtTonJEVp7gtu/mrpw2Fx5HQOzu5prqg4uQtcSzVSsGLTSami/sgHF8aMr6LPv0UkYKBfHiYP7HPtb7bbWCn7hxt7qs7r2HYcFBWL1SH/yQT03w6rKa4/ywSZCX567VB7kKVSL4+LoWtSvRPiNzBwN02pbH6a1MAzWc8VFvlIuD05oJmpUnq+zlpWKW/fgFurVhd6nKFTatxONZvr/UpS8wju5YRat0kQSUD1W4yA6nTlItGtXRLfu7Ob/FlXRToNx0x0Uh6kfNiI0ynrMjlVA4nVNyIqJornHnjx2S5QTQpuV6WGABu6f7KmynRsskIjpq+RacdoF8IZoBjr5Kli3ERvQUbJfLnRmygJV8/F00b+k6MVrA==", Base64.DEFAULT);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
            GZIPInputStream gis = new GZIPInputStream(bis);
            byte[] buffer = new byte[4096];
            int bytesRead;
            StringBuilder sb = new StringBuilder();
            while ((bytesRead = gis.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, bytesRead));
            }
            gis.close();
            bis.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
