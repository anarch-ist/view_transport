<?php
/**
 * Автотест помощника для работы с дробными числами.
 *
 * @category Rmk
 * @package  Rmk_Util
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Util;

use Rmk\Util\FloatHelper,
    PHPUnit_Framework_TestCase;

class FloatHelperTest extends PHPUnit_Framework_TestCase
{

    public function testIsFloat()
    {
        $float  = 25.3;
        $string = 'std';
        $array  = array();

        $this->assertTrue(FloatHelper::isFloat($float));

        $this->assertFalse(FloatHelper::isFloat($string));
        $this->assertFalse(FloatHelper::isFloat($array));
    }

    public function testIsPositive()
    {
        $positiveFloat = 25.3;
        $negativeInt   = -1;
        $negativeFloat = -0.1;
        $zero          = 0;
        $string        = 'std';
        $array         = array();

        $this->assertTrue(FloatHelper::isPositive($positiveFloat));

        $this->assertFalse(FloatHelper::isPositive($negativeInt));
        $this->assertFalse(FloatHelper::isPositive($negativeFloat));
        $this->assertFalse(FloatHelper::isPositive($zero));
        $this->assertFalse(FloatHelper::isPositive($string));
        $this->assertFalse(FloatHelper::isPositive($array));
    }

    public function testIsNegative()
    {
        $negativeFloat = -0.1;
        $positiveFloat = 0.35;
        $negativeInt   = -1;
        $positiveInt   = 25;
        $zero          = 0;
        $string        = 'std';
        $array         = array();

        $this->assertTrue(FloatHelper::isNegative($negativeFloat));

        $this->assertFalse(FloatHelper::isNegative($positiveFloat));
        $this->assertFalse(FloatHelper::isNegative($negativeInt));
        $this->assertFalse(FloatHelper::isNegative($positiveInt));
        $this->assertFalse(FloatHelper::isNegative($zero));
        $this->assertFalse(FloatHelper::isNegative($string));
        $this->assertFalse(FloatHelper::isNegative($array));
    }

    public function testIsBetween()
    {
        $float = 25.3;

        $this->assertTrue(FloatHelper::isBetween($float, 0, 30));
        $this->assertTrue(FloatHelper::isBetween($float, -1, 80));

        $this->assertFalse(FloatHelper::isBetween($float, 0, 25));
        $this->assertFalse(FloatHelper::isBetween($float, 30, 0));
    }

    public function testIsInRange()
    {
        $float = 25.3;

        $this->assertTrue(FloatHelper::isInRange($float, 0, 30));
        $this->assertTrue(FloatHelper::isInRange($float, -1, 80));
        $this->assertTrue(FloatHelper::isInRange($float, 0, 25.3));

        $this->assertFalse(FloatHelper::isInRange($float, 30, 0));
    }

    public function testIsZero()
    {
        $zero        = 0;
        $floatZero   = 0.0;
        $doubleZero  = 0.00;
        $positiveInt = 25;
        $negativeInt = 25;

        $this->assertTrue(FloatHelper::isZero($floatZero));
        $this->assertTrue(FloatHelper::isZero($doubleZero));

        $this->assertFalse(FloatHelper::isZero($zero));
        $this->assertFalse(FloatHelper::isZero($positiveInt));
        $this->assertFalse(FloatHelper::isZero($negativeInt));
    }

    public function testEnsureFloat()
    {
        $float = 25.3;

        $ensuredFloat = &FloatHelper::ensureFloat($float);

        $ensuredFloat = 23.3;

        $this->assertEquals(23.3, $float);

        FloatHelper::ensureFloat($float);
    }

    public function testEnsureFloatNotFloat()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeFloat = array();

        FloatHelper::ensureFloat($mayBeFloat);
    }

    public function testEnsurePositive()
    {
        $float = 25.5;

        $ensuredFloat = &FloatHelper::ensurePositive($float);

        $ensuredFloat = 23.8;

        $this->assertEquals(23.8, $float);

        FloatHelper::ensurePositive($float);
    }

    public function testEnsurePositiveNotFloat()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeFloat = array();

        FloatHelper::ensurePositive($mayBeFloat);
    }

    public function testEnsurePositiveNotPositive()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBePositiveFloat = -25.4;

        FloatHelper::ensurePositive($mayBePositiveFloat);
    }

    public function testEnsureNegative()
    {
        $float = -25.3;

        $ensuredFloat = &FloatHelper::ensureNegative($float);

        $ensuredFloat = -23.6;

        $this->assertEquals(-23.6, $float);

        FloatHelper::ensureNegative($float);
    }

    public function testEnsureNegativeNotFloat()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeFloat = array();

        FloatHelper::ensureNegative($mayBeFloat);
    }

    public function testEnsureNegativeNotNegative()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeNegativeFloat = 25.3;

        FloatHelper::ensureNegative($mayBeNegativeFloat);
    }

    public function testEnsureBetween()
    {
        $float = 5.3;

        $ensuredBetween = &FloatHelper::ensureBetween($float, 0, 6);

        $ensuredBetween = 4.3;

        $this->assertEquals(4.3, $float);
    }

    public function testEnsureBetweenNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeFloat = array();

        FloatHelper::ensureBetween($mayBeFloat, 0, 10);
    }

    public function testEnsureBetweenRangeErrorLess()
    {
        $this->setExpectedException('UnexpectedValueException');

        $float = 5.2;

        FloatHelper::ensureBetween($float, 0, -3);
    }

    public function testEnsureBetweenRangeErrorEqual()
    {
        $this->setExpectedException('UnexpectedValueException');

        $float = 5.2;

        FloatHelper::ensureBetween($float, 0, 0);
    }

    public function testEnsureBetweenLess()
    {
        $this->setExpectedException('OutOfRangeException');

        $float = 5.2;

        FloatHelper::ensureBetween($float, 6, 10);
    }

    public function testEnsureBetweenLessOrEqual()
    {
        $this->setExpectedException('OutOfRangeException');

        $float = 5.2;

        FloatHelper::ensureBetween($float, 5.2, 10);
    }

    public function testEnsureBetweenMore()
    {
        $this->setExpectedException('OutOfRangeException');

        $float = 5.2;

        FloatHelper::ensureBetween($float, 0, 3);
    }

    public function testEnsureBetweenMoreOrEqual()
    {
        $this->setExpectedException('OutOfRangeException');

        $int = 5.2;

        FloatHelper::ensureBetween($int, 0, 5.2);
    }

    public function testEnsureInRange()
    {
        $float = 5.2;

        $ensuredInRange = &FloatHelper::ensureInRange($float, 0, 5.3);

        $ensuredInRange = 4.1;

        $this->assertEquals(4.1, $float);
    }

    public function testEnsureInRangeNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeFloat = array();

        FloatHelper::ensureInRange($mayBeFloat, 0, 10);
    }

    public function testEnsureInRangeRangeErrorLess()
    {
        $this->setExpectedException('UnexpectedValueException');

        $float = 5.2;

        FloatHelper::ensureInRange($float, 0, -3);
    }

    public function testEnsureInRangeRangeEqual()
    {
        $float = 5.2;

        $ensuredFloat = FloatHelper::ensureInRange($float, 5, 5.2);

        $this->assertEquals(5.2, $ensuredFloat);
    }

    public function testEnsureInRangeLess()
    {
        $this->setExpectedException('OutOfRangeException');

        $float = 5.2;

        FloatHelper::ensureInRange($float, 6, 10);
    }

    public function testEnsureInRangeLessOrEqual()
    {
        $float = 5.3;

        $this->assertEquals(5.3, FloatHelper::ensureInRange($float, 5.3, 10));
    }

    public function testEnsureInRangeMore()
    {
        $this->setExpectedException('OutOfRangeException');

        $float = 5.2;

        FloatHelper::ensureInRange($float, 0, 3);
    }

    public function testEnsureInRangeMoreOrEqual()
    {
        $float = 5.2;

        $this->assertEquals(5.2, FloatHelper::ensureInRange($float, 0, 5.2));
    }

    public function testEnsureZero()
    {
        $zero = 0.0;

        $ensuredZero = &FloatHelper::ensureZero($zero);

        $ensuredZero = 23;

        $this->assertEquals(23, $zero);
    }

    public function testEnsureZeroNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeZero = array();

        FloatHelper::ensureZero($mayBeZero);
    }

    public function testEnsureZeroNotZero()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeZero = 25.2;

        FloatHelper::ensureZero($mayBeZero);
    }

}