<?php
/**
 * Автотест помощника для работы с числовыми значениями.
 *
 * @category Rmk
 * @package  Rmk_Util
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Util;

use Rmk\Util\NumericHelper,
    PHPUnit_Framework_TestCase;

class NumericHelperTest extends PHPUnit_Framework_TestCase
{

    public function testIsNumber()
    {
        $stringNumber = '32';
        $float        = 25.3;
        $string       = 'std';
        $array        = array();

        $this->assertTrue(NumericHelper::isNumber($stringNumber));
        $this->assertTrue(NumericHelper::isNumber($float));

        $this->assertFalse(NumericHelper::isNumber($string));
        $this->assertFalse(NumericHelper::isNumber($array));
    }

    public function testIsPositive()
    {
        $positiveStringFloat = '25.3';
        $positiveFloat       = 25.3;
        $negativeInt         = -1;
        $negativeFloat       = -0.1;
        $zero                = 0;
        $string              = 'std';
        $array               = array();

        $this->assertTrue(NumericHelper::isPositive($positiveStringFloat));
        $this->assertTrue(NumericHelper::isPositive($positiveFloat));

        $this->assertFalse(NumericHelper::isPositive($negativeInt));
        $this->assertFalse(NumericHelper::isPositive($negativeFloat));
        $this->assertFalse(NumericHelper::isPositive($zero));
        $this->assertFalse(NumericHelper::isPositive($string));
        $this->assertFalse(NumericHelper::isPositive($array));
    }

    public function testIsNegative()
    {
        $negativeStringFloat = '-0.1';
        $negativeFloat       = -0.1;
        $positiveFloat       = 0.35;
        $negativeInt         = -1;
        $positiveInt         = 25;
        $zero                = 0;
        $string              = 'std';
        $array               = array();

        $this->assertTrue(NumericHelper::isNegative($negativeStringFloat));
        $this->assertTrue(NumericHelper::isNegative($negativeFloat));
        $this->assertTrue(NumericHelper::isNegative($negativeInt));

        $this->assertFalse(NumericHelper::isNegative($positiveFloat));
        $this->assertFalse(NumericHelper::isNegative($positiveInt));
        $this->assertFalse(NumericHelper::isNegative($zero));
        $this->assertFalse(NumericHelper::isNegative($string));
        $this->assertFalse(NumericHelper::isNegative($array));
    }

    public function testIsBetween()
    {
        $float = 25.3;

        $this->assertTrue(NumericHelper::isBetween($float, '0', '30'));
        $this->assertTrue(NumericHelper::isBetween($float, '-1', 80));

        $this->assertFalse(NumericHelper::isBetween($float, 0, 25));
        $this->assertFalse(NumericHelper::isBetween($float, 30, 0));
    }

    public function testIsInRange()
    {
        $float = 25.3;

        $this->assertTrue(NumericHelper::isInRange($float, 0, 30));
        $this->assertTrue(NumericHelper::isInRange($float, -1, 80));
        $this->assertTrue(NumericHelper::isInRange($float, '0', '25.3'));

        $this->assertFalse(NumericHelper::isInRange($float, '-25', 10.11));
        $this->assertFalse(NumericHelper::isInRange($float, 30, 0));
    }

    public function testIsZero()
    {
        $zero        = 0;
        $floatZero   = 0.0;
        $doubleZero  = 0.00;
        $stringZero  = '0';
        $positiveInt = 25;
        $negativeInt = 25;

        $this->assertTrue(NumericHelper::isZero($zero));
        $this->assertTrue(NumericHelper::isZero($floatZero));
        $this->assertTrue(NumericHelper::isZero($doubleZero));
        $this->assertTrue(NumericHelper::isZero($stringZero));

        $this->assertFalse(NumericHelper::isZero($positiveInt));
        $this->assertFalse(NumericHelper::isZero($negativeInt));
    }

    public function testEnsureNumber()
    {
        $number = '25.3';

        $ensuredNumber = &NumericHelper::ensureNumber($number);

        $ensuredNumber = '23.3';

        $this->assertEquals('23.3', $number);

        NumericHelper::ensureNumber($number);
    }

    public function testEnsureNumberNotNumber()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeNumber = array();

        NumericHelper::ensureNumber($mayBeNumber);
    }

    public function testEnsurePositive()
    {
        $number = '25.5';

        $ensuredNumber = &NumericHelper::ensurePositive($number);

        $ensuredNumber = '23.8';

        $this->assertEquals('23.8', $number);

        NumericHelper::ensurePositive($number);
    }

    public function testEnsurePositiveNotNumber()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBePositiveNumber = array();

        NumericHelper::ensurePositive($mayBePositiveNumber);
    }

    public function testEnsurePositiveNotPositive()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBePositiveFloat = -25.4;

        NumericHelper::ensurePositive($mayBePositiveFloat);
    }

    public function testEnsureNegative()
    {
        $number = '-25.3';

        $ensuredNumber = &NumericHelper::ensureNegative($number);

        $ensuredNumber = '-23.6';

        $this->assertEquals('-23.6', $number);

        NumericHelper::ensureNegative($number);
    }

    public function testEnsureNegativeNotNumber()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeNumber = array();

        NumericHelper::ensureNegative($mayBeNumber);
    }

    public function testEnsureNegativeNotNegative()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeNegativeFloat = '25.3';

        NumericHelper::ensureNegative($mayBeNegativeFloat);
    }

    public function testEnsureBetween()
    {
        $number = '5.3';

        $ensuredBetween = &NumericHelper::ensureBetween($number, 0, 6);

        $ensuredBetween = 4.3;

        $this->assertEquals(4.3, $number);
    }

    public function testEnsureBetweenNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeFloat = array();

        NumericHelper::ensureBetween($mayBeFloat, 0, 10);
    }

    public function testEnsureBetweenRangeErrorLess()
    {
        $this->setExpectedException('UnexpectedValueException');

        $float = 5.2;

        NumericHelper::ensureBetween($float, 0, -3);
    }

    public function testEnsureBetweenRangeErrorEqual()
    {
        $this->setExpectedException('UnexpectedValueException');

        $float = 5.2;

        NumericHelper::ensureBetween($float, 0, 0);
    }

    public function testEnsureBetweenLess()
    {
        $this->setExpectedException('OutOfRangeException');

        $float = 5.2;

        NumericHelper::ensureBetween($float, '6', 10.1);
    }

    public function testEnsureBetweenLessOrEqual()
    {
        $this->setExpectedException('OutOfRangeException');

        $float = '5.2';

        NumericHelper::ensureBetween($float, '5.2', 10);
    }

    public function testEnsureBetweenMore()
    {
        $this->setExpectedException('OutOfRangeException');

        $float = '5.2';

        NumericHelper::ensureBetween($float, 0, 3);
    }

    public function testEnsureBetweenMoreOrEqual()
    {
        $this->setExpectedException('OutOfRangeException');

        $int = '5.2';

        NumericHelper::ensureBetween($int, 0, 5.2);
    }

    public function testEnsureInRange()
    {
        $number = '5.2';

        $ensuredInRange = &NumericHelper::ensureInRange($number, '0', 5.3);

        $ensuredInRange = 4.1;

        $this->assertEquals(4.1, $number);
    }

    public function testEnsureInRangeNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeNumber = array();

        NumericHelper::ensureInRange($mayBeNumber, 0, 10);
    }

    public function testEnsureInRangeRangeErrorLess()
    {
        $this->setExpectedException('UnexpectedValueException');

        $float = 5.2;

        NumericHelper::ensureInRange($float, 0, -3);
    }

    public function testEnsureInRangeRangeEqual()
    {
        $float = '5.2';

        $ensuredNumber = NumericHelper::ensureInRange($float, '5', 5.2);

        $this->assertEquals('5.2', $ensuredNumber);
    }

    public function testEnsureInRangeLess()
    {
        $this->setExpectedException('OutOfRangeException');

        $float = 5.2;

        NumericHelper::ensureInRange($float, 6, 10);
    }

    public function testEnsureInRangeLessOrEqual()
    {
        $float = 5.3;

        $this->assertEquals(5.3, NumericHelper::ensureInRange($float, 5.3, 10));
    }

    public function testEnsureInRangeMore()
    {
        $this->setExpectedException('OutOfRangeException');

        $float = 5.2;

        NumericHelper::ensureInRange($float, 0, 3);
    }

    public function testEnsureInRangeMoreOrEqual()
    {
        $float = 5.2;

        $this->assertEquals(5.2, NumericHelper::ensureInRange($float, 0, 5.2));
    }

    public function testEnsureZero()
    {
        $zero = 0.0;

        $ensuredZero = &NumericHelper::ensureZero($zero);

        $ensuredZero = 23;

        $this->assertEquals(23, $zero);
    }

    public function testEnsureZeroNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeZero = array();

        NumericHelper::ensureZero($mayBeZero);
    }

    public function testEnsureZeroNotZero()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeZero = 25.2;

        NumericHelper::ensureZero($mayBeZero);
    }

}