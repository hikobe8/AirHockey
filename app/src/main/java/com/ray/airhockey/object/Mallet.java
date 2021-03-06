package com.ray.airhockey.object;

import com.ray.airhockey.data.VertexArray;
import com.ray.airhockey.program.ColorShaderProgram;
import com.ray.airhockey.util.Geometry;

import java.util.List;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-05 15:18
 *  description : 
 */
public class Mallet {

    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float mRadius, mHeight;

    private final VertexArray mVertexArray;

    private final List<ObjectBuilder.DrawCommand> mDrawCommandList;

    public Mallet(float radius, float height, int numPointsAroundMallet) {
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createMallet(
                new Geometry.Point(0f, 0f, 0f), radius, height,
                numPointsAroundMallet);
        mRadius = radius;
        mHeight = height;
        mVertexArray = new VertexArray(generatedData.mVertexData);
        mDrawCommandList = generatedData.mDrawCommands;
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        mVertexArray.setVertexAttribPointer(
                0,
                colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                0);
    }

    public void draw(){
        for (ObjectBuilder.DrawCommand drawCommand : mDrawCommandList) {
            drawCommand.draw();
        }
    }

}
