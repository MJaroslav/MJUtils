package io.github.mjaroslav.mjutils.client.model.obj;

import io.github.mjaroslav.mjutils.util.object.game.ResourcePath;
import lombok.val;
import net.minecraftforge.client.model.obj.*;
import org.junit.Assert;
import org.junit.Test;

public class TestIconScaledWavefrontObject {
    @Test
    public void load() {
        val path = ResourcePath.full("/io/github/mjaroslav/mjutils/client/model/obj/TestIconScaledWavefrontObject.obj");
        val expected = new WavefrontObject(path.getPath(), path.stream());
        val actual = new IconScaledWavefrontObject(path.getPath(), path.stream());
        val count = expected.groupObjects.size();
        Assert.assertEquals("Objects/Groups count", count, actual.getGroupObjects().size());
        if (count > 0)
            Assert.assertEquals("GLDrawingMode", expected.groupObjects.get(0).glDrawingMode, actual.getGlDrawingMode());

        // iterable elements, lol
        GroupObject expectedGroup;
        IconScaledGroupObject actualGroup;
        Face expectedFace;
        IconScaledFace actualFace;
        int i, j, k, faceCount, valuesCount;
        val expectedValues = new float[3];
        val actualValues = new float[3];
        Vertex expectedVertex, actualVertex;
        TextureCoordinate expectedTextureCoordinate, actualTextureCoordinate;
        String name;


        for (i = 0; i < count; i++) {
            expectedGroup = expected.groupObjects.get(i);
            name = expectedGroup.name;
            actualGroup = actual.getGroupObjects().get(i);
            Assert.assertEquals(i + " Object/Group name", name, actualGroup.getName());
            faceCount = expectedGroup.faces.size();
            Assert.assertEquals(i + " Object/Group faces count", faceCount, actualGroup.getFaces().size());
            for (j = 0; j < count; j++) {
                expectedFace = expectedGroup.faces.get(j);
                actualFace = actualGroup.getFaces().get(j);
                valuesCount = expectedFace.vertices.length;
                Assert.assertEquals(j + " face of " + name + " Group/Object vertices count", valuesCount, actualFace
                    .getVertices().length);
                for (k = 0; k < valuesCount; k++) {
                    expectedVertex = expectedFace.vertices[k];
                    actualVertex = actualFace.getVertices()[k];
                    expectedValues[0] = expectedVertex.x;
                    expectedValues[1] = expectedVertex.y;
                    expectedValues[2] = expectedVertex.z;
                    actualValues[0] = actualVertex.x;
                    actualValues[1] = actualVertex.y;
                    actualValues[2] = actualVertex.z;
                    Assert.assertArrayEquals(k + " vertex from " + j + " face of " + name + " Group/Object",
                        expectedValues, actualValues, 0);
                }
                if (expectedFace.vertexNormals == null)
                    Assert.assertNull(j + " face of " + name + " Group/Object vertex normals",
                        actualFace.getVertexNormals());
                else {
                    Assert.assertNotNull(j + " face of " + name + " Group/Object vertex normals",
                        actualFace.getVertexNormals());
                    valuesCount = expectedFace.vertexNormals.length;
                    Assert.assertEquals(j + " face of " + name + " Group/Object vertices count", valuesCount, actualFace
                        .getVertexNormals().length);
                    for (k = 0; k < valuesCount; k++) {
                        expectedVertex = expectedFace.vertices[k];
                        actualVertex = actualFace.getVertices()[k];
                        expectedValues[0] = expectedVertex.x;
                        expectedValues[1] = expectedVertex.y;
                        expectedValues[2] = expectedVertex.z;
                        actualValues[0] = actualVertex.x;
                        actualValues[1] = actualVertex.y;
                        actualValues[2] = actualVertex.z;
                        Assert.assertArrayEquals(k + " vertex normal from " + j + " face of " + name + " Group/Object",
                            expectedValues, actualValues, 0);
                    }
                }
                if (expectedFace.textureCoordinates == null)
                    Assert.assertNull(j + " face of " + name + " Group/Object texture coordinates",
                        actualFace.getTextureCoordinates());
                else {
                    Assert.assertNotNull(j + " face of " + name + " Group/Object texture coordinates",
                        actualFace.getTextureCoordinates());
                    valuesCount = expectedFace.textureCoordinates.length;
                    Assert.assertEquals(j + " face of " + name + " Group/Object texture coordinates count", valuesCount,
                        actualFace.getTextureCoordinates().length);
                    for (k = 0; k < valuesCount; k++) {
                        expectedTextureCoordinate = expectedFace.textureCoordinates[k];
                        actualTextureCoordinate = actualFace.getTextureCoordinates()[k];
                        expectedValues[0] = expectedTextureCoordinate.u;
                        expectedValues[1] = expectedTextureCoordinate.v;
                        expectedValues[2] = expectedTextureCoordinate.w;
                        actualValues[0] = actualTextureCoordinate.u;
                        actualValues[1] = actualTextureCoordinate.v;
                        actualValues[2] = actualTextureCoordinate.w;
                        Assert.assertArrayEquals(k + " texture coordinate from " + j + " face of " + name +
                            " Group/Object", expectedValues, actualValues, 0);
                    }
                }
                Assert.assertNotNull("Unexpected null in face normal from " + k + " face of " + name + " Group/Object",
                    expectedFace.faceNormal);
                expectedVertex = expectedFace.faceNormal;
                actualVertex = actualFace.getFaceNormal().orElseThrow();
                expectedValues[0] = expectedVertex.x;
                expectedValues[1] = expectedVertex.y;
                expectedValues[2] = expectedVertex.z;
                actualValues[0] = actualVertex.x;
                actualValues[1] = actualVertex.y;
                actualValues[2] = actualVertex.z;
                Assert.assertArrayEquals("Face normal from " + j + " face of " + name + " Group/Object",
                    expectedValues, actualValues, 0);
            }
        }
    }
}
