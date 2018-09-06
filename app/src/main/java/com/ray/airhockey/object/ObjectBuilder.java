package com.ray.airhockey.object;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static com.ray.airhockey.util.Geometry.Circle;
import static com.ray.airhockey.util.Geometry.Cylinder;
import static com.ray.airhockey.util.Geometry.Point;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-06 10:10
 *  description : 
 */
public class ObjectBuilder {

    private static final int FLOATS_PER_VERTEX = 3;

    interface DrawCommand{
        void draw();
    }

    static class GeneratedData{

        final float[] mVertexData;
        final List<DrawCommand> mDrawCommands;

        public GeneratedData(float[] vertexData, List<DrawCommand> drawCommands) {
            mVertexData = vertexData;
            mDrawCommands = drawCommands;
        }
    }

    private final float[] mVertexData;
    private final List<DrawCommand> mDrawList = new ArrayList<>();
    private int mOffset = 0;

    private ObjectBuilder(int sizeInVertices) {
        mVertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    public static int sizeOfCircleInVertices(int numberPoint) {
        return 1 + (numberPoint + 1);
    }

    public static int sizeOfOpenCylinderInVertices(int numberPoint) {
        return (numberPoint + 1) * 2;
    }

    static GeneratedData createPuck(Cylinder cylinder, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints);
        ObjectBuilder objectBuilder = new ObjectBuilder(size);
        Circle puckTop = new Circle(
                cylinder.center.translateY(cylinder.height/2f),
                cylinder.radius);
        objectBuilder.appendCircle(puckTop, numPoints);
        objectBuilder.appendOpenCylinder(cylinder, numPoints);
        return objectBuilder.build();
    }

    static GeneratedData createMallet(Point center, float radius, float height, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(numPoints) * 2;
        ObjectBuilder builder = new ObjectBuilder(size);

        //first generate the mallet base
        float baseHeight = height * 0.25f;
        Circle baseCircle = new Circle(center.translateY(-baseHeight), radius);
        Cylinder baseCylinder = new Cylinder(baseCircle.center.translateY(-baseHeight/2f), radius, baseHeight);
        builder.appendCircle(baseCircle, numPoints);
        builder.appendOpenCylinder(baseCylinder, numPoints);
        //then generate the mallet handle
        float handleHeight = height * 0.75f;
        float handleRadius = radius / 3f;
        Circle handleCircle = new Circle(center.translateY(height/2f), handleRadius);
        Cylinder handleCylinder = new Cylinder(handleCircle.center.translateY(-handleHeight/2f), handleRadius, handleHeight);
        builder.appendCircle(handleCircle,numPoints);
        builder.appendOpenCylinder(handleCylinder, numPoints);
        return builder.build();
    }

    private GeneratedData build(){
        return new GeneratedData(mVertexData, mDrawList);
    }

    private void appendCircle(Circle circle, int numPoints){
        final int startVertex = mOffset / FLOATS_PER_VERTEX;
        final int numberVertices = sizeOfCircleInVertices(numPoints);
        //center point of fan
        mVertexData[mOffset++] = circle.center.x;
        mVertexData[mOffset++] = circle.center.y;
        mVertexData[mOffset++] = circle.center.z;

        for (int i = 0; i <= numPoints; i++) {
            float angleInRadians =  (i*1f / numPoints * (float)(Math.PI *2f));
            mVertexData[mOffset++] = (float) (circle.center.x + circle.radius* Math.cos(angleInRadians));
            mVertexData[mOffset++] = circle.center.y;
            mVertexData[mOffset++] = (float) (circle.center.z + circle.radius* Math.sin(angleInRadians));
        }
        mDrawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numberVertices);
            }
        });
    }

    private void appendOpenCylinder(Cylinder cylinder, int numPoints){
        final int startVertex = mOffset / FLOATS_PER_VERTEX;
        final int numberVertices = sizeOfOpenCylinderInVertices(numPoints);
        final float yStart = cylinder.center.y - (cylinder.height/2f);
        final float yEnd = cylinder.center.y + (cylinder.height/2f);
        for (int i = 0; i <= numPoints; i++) {
            float angleInRadians = i*1f/numPoints * (float) Math.PI*2f;
            float xPosition = cylinder.center.x + cylinder.radius * (float)Math.cos(angleInRadians);
            float zPosition = cylinder.center.z + cylinder.radius * (float) Math.sin(angleInRadians);

            mVertexData[mOffset ++] = xPosition;
            mVertexData[mOffset ++] = yStart;
            mVertexData[mOffset ++] = zPosition;

            mVertexData[mOffset ++] = xPosition;
            mVertexData[mOffset ++] = yEnd;
            mVertexData[mOffset ++] = zPosition;
        }
        mDrawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numberVertices);
            }
        });

    }

}
