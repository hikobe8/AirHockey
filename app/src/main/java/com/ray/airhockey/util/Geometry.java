package com.ray.airhockey.util;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-06 9:59
 *  description : 
 */
public class Geometry {

    public static class Point {

        public final float x, y, z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }

        public Point translate(Vector vector) {
            return new Point(x + vector.x, y + vector.y, z + vector.z);
        }
    }

    public static class Circle {
        public final Point center;
        public final float radius;

        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }

    }

    public static class Cylinder {
        public final Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
    }

    public static class Sphere {
        public final Point center;
        public final float radius;

        public Sphere(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }
    }

    public static class Ray {

        public final Point mPoint;
        public final Vector mVector;

        public Ray(Point point, Vector vector) {
            mPoint = point;
            mVector = vector;
        }
    }

    public static class Vector {

        public final float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float length() {
            return (float) Math.sqrt(x * x + y * y + z * z);
        }

        public Vector crossProduct(Vector other) {
            return new Vector(
                    this.y * other.z - this.z * other.y,
                    this.z * other.x - this.x * other.z,
                    this.x * other.y - this.y * other.x
            );
        }

    }

    public static Vector vectorBetween(Point from, Point to) {
        return new Vector(
                to.x - from.x,
                to.y - from.y,
                to.z - from.z
        );
    }

    public static boolean intersects(Sphere sphere, Ray ray) {
        return distanceBetween(sphere.center, ray) < sphere.radius;
    }

    private static float distanceBetween(Point point, Ray ray) {
        Vector p1ToPoint = vectorBetween(ray.mPoint, point);
        Vector p2ToPoint = vectorBetween(ray.mPoint.translate(ray.mVector), point);

        float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
        float lengthOfBase = ray.mVector.length();
        return areaOfTriangleTimesTwo / lengthOfBase;
    }


}
