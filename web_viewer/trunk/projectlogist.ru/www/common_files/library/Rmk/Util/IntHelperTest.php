<?php
/**
 * Автотест помощника для работы с целыми числами.
 *
 * @category Rmk
 * @package  Rmk_Util
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Util;

use Rmk\Util\IntHelper,
    PHPUnit_Framework_TestCase;

class IntHelperTest extends PHPUnit_Framework_TestCase
{

    public function testIsInt()
    {
        $int    = 25;
        $string = 'std';
        $array  = array();

        $this->assertTrue(IntHelper::isInt($int));

        $this->assertFalse(IntHelper::isInt($string));
        $this->assertFalse(IntHelper::isInt($array));
    }

    public function testIsPositive()
    {
        $positiveInt   = 25;
        $negativeInt   = -0.1;
        $negativeFloat = -0.1;
        $zero          = 0;
        $string        = 'std';
        $array         = array();

        $this->assertTrue(IntHelper::isPositive($positiveInt));

        $this->assertFalse(IntHelper::isPositive($negativeInt));
        $this->assertFalse(IntHelper::isPositive($negativeFloat));
        $this->assertFalse(IntHelper::isPositive($zero));
        $this->assertFalse(IntHelper::isPositive($string));
        $this->assertFalse(IntHelper::isPositive($array));
    }

    public function testIsNegative()
    {
        $negativeInt   = -1;
        $negativeFloat = -0.1;
        $positiveInt   = 25;
        $zero          = 0;
        $string        = 'std';
        $array         = array();

        $this->assertTrue(IntHelper::isNegative($negativeInt));

        $this->assertFalse(IntHelper::isNegative($negativeFloat));
        $this->assertFalse(IntHelper::isNegative($positiveInt));
        $this->assertFalse(IntHelper::isNegative($zero));
        $this->assertFalse(IntHelper::isNegative($string));
        $this->assertFalse(IntHelper::isNegative($array));
    }

    public function testIsBetween()
    {
        $int = 25;

        $this->assertTrue(IntHelper::isBetween($int, 0, 30));
        $this->assertTrue(IntHelper::isBetween($int, -1, 80));

        $this->assertFalse(IntHelper::isBetween($int, 0, 25));
        $this->assertFalse(IntHelper::isBetween($int, 30, 0));
    }

    public function testIsInRange()
    {
        $int = 25;

        $this->assertTrue(IntHelper::isInRange($int, 0, 30));
        $this->assertTrue(IntHelper::isInRange($int, -1, 80));
        $this->assertTrue(IntHelper::isInRange($int, 0, 25));

        $this->assertFalse(IntHelper::isInRange($int, 30, 0));
    }

    public function testIsZero()
    {
        $zero        = 0;
        $floatZero   = 0.0;
        $positiveInt = 25;
        $negativeInt = 25;

        $this->assertTrue(IntHelper::isZero($zero));

        $this->assertFalse(IntHelper::isZero($floatZero));
        $this->assertFalse(IntHelper::isZero($positiveInt));
        $this->assertFalse(IntHelper::isZero($negativeInt));
    }

    public function testIsMoreThan()
    {
        $this->assertTrue(IntHelper::isMoreThan(0, -1));
        $this->assertTrue(IntHelper::isMoreThan(25, 2));
        $this->assertTrue(IntHelper::isMoreThan(23, 0));

        $this->assertFalse(IntHelper::isMoreThan(1, 3));
        $this->assertFalse(IntHelper::isMoreThan(-1, 0));
        $this->assertFalse(IntHelper::isMoreThan(0, 0));
        $this->assertFalse(IntHelper::isMoreThan(0, '123'));
        $this->assertFalse(IntHelper::isMoreThan(0, '-24'));
    }

    public function testIsMoreThanOrEquals()
    {
        $this->assertTrue(IntHelper::isMoreThanOrEquals(0, -1));
        $this->assertTrue(IntHelper::isMoreThanOrEquals(25, 2));
        $this->assertTrue(IntHelper::isMoreThanOrEquals(23, 0));
        $this->assertTrue(IntHelper::isMoreThanOrEquals(0, 0));

        $this->assertFalse(IntHelper::isMoreThanOrEquals(1, 3));
        $this->assertFalse(IntHelper::isMoreThanOrEquals(-1, 0));
        $this->assertFalse(IntHelper::isMoreThanOrEquals(0, '123'));
        $this->assertFalse(IntHelper::isMoreThanOrEquals(0, '-24'));
    }

    public function testIsLessThan()
    {
        $this->assertTrue(IntHelper::isLessThan(1, 3));
        $this->assertTrue(IntHelper::isLessThan(-1, 0));

        $this->assertFalse(IntHelper::isLessThan(0, 0));
        $this->assertFalse(IntHelper::isLessThan(0, -1));
        $this->assertFalse(IntHelper::isLessThan(25, 2));
        $this->assertFalse(IntHelper::isLessThan(23, 0));
        $this->assertFalse(IntHelper::isLessThan(0, '123'));
        $this->assertFalse(IntHelper::isLessThan(0, '-24'));
    }

    public function testIsLessThanOrEquals()
    {
        $this->assertTrue(IntHelper::isLessThanOrEquals(0, 0));
        $this->assertTrue(IntHelper::isLessThanOrEquals(-1, 0));
        $this->assertTrue(IntHelper::isLessThanOrEquals(1, 3));

        $this->assertFalse(IntHelper::isLessThanOrEquals(0, -1));
        $this->assertFalse(IntHelper::isLessThanOrEquals(25, 2));
        $this->assertFalse(IntHelper::isLessThanOrEquals(23, 0));
        $this->assertFalse(IntHelper::isLessThanOrEquals(0, '123'));
        $this->assertFalse(IntHelper::isLessThanOrEquals(0, '-24'));
    }

    public function testIsMoreThanZero()
    {
        $this->assertTrue(IntHelper::isMoreThanZero(1));
        $this->assertTrue(IntHelper::isMoreThanZero(25));
        $this->assertTrue(IntHelper::isMoreThanZero(33));

        $this->assertFalse(IntHelper::isMoreThanZero(0));
        $this->assertFalse(IntHelper::isMoreThanZero(-1));
        $this->assertFalse(IntHelper::isMoreThanZero('25'));
    }

    public function testIsMoreThanOrEqualsZero()
    {
        $this->assertTrue(IntHelper::isMoreThanOrEqualsZero(1));
        $this->assertTrue(IntHelper::isMoreThanOrEqualsZero(25));
        $this->assertTrue(IntHelper::isMoreThanOrEqualsZero(33));
        $this->assertTrue(IntHelper::isMoreThanOrEqualsZero(0));

        $this->assertFalse(IntHelper::isMoreThanOrEqualsZero(-1));
        $this->assertFalse(IntHelper::isMoreThanOrEqualsZero('25'));
    }

    public function testIsLessThanZero()
    {
        $this->assertTrue(IntHelper::isLessThanZero(-1));

        $this->assertFalse(IntHelper::isLessThanZero(1));
        $this->assertFalse(IntHelper::isLessThanZero(25));
        $this->assertFalse(IntHelper::isLessThanZero(33));
        $this->assertFalse(IntHelper::isLessThanZero('25'));
        $this->assertFalse(IntHelper::isLessThanZero(0));
    }

    public function testIsLessThanOrEqualsZero()
    {
        $this->assertTrue(IntHelper::isLessThanOrEqualsZero(-1));
        $this->assertTrue(IntHelper::isLessThanOrEqualsZero(0));

        $this->assertFalse(IntHelper::isLessThanOrEqualsZero(1));
        $this->assertFalse(IntHelper::isLessThanOrEqualsZero(25));
        $this->assertFalse(IntHelper::isLessThanOrEqualsZero(33));
        $this->assertFalse(IntHelper::isLessThanOrEqualsZero('25'));
    }

    public function testEnsureInt()
    {
        $int = 25;

        $ensuredInt = &IntHelper::ensureInt($int);

        $ensuredInt = 23;

        $this->assertEquals(23, $int);

        IntHelper::ensureInt($int);
    }

    public function testEnsureIntNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = array();

        IntHelper::ensureInt($mayBeInt);
    }

    public function testEnsurePositive()
    {
        $int = 25;

        $ensuredInt = &IntHelper::ensurePositive($int);

        $ensuredInt = 23;

        $this->assertEquals(23, $int);

        IntHelper::ensurePositive($int);
    }

    public function testEnsurePositiveNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = array();

        IntHelper::ensurePositive($mayBeInt);
    }

    public function testEnsurePositiveNotPositive()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBePositiveInt = -25;

        IntHelper::ensurePositive($mayBePositiveInt);
    }

    public function testEnsureNegative()
    {
        $int = -25;

        $ensuredInt = &IntHelper::ensureNegative($int);

        $ensuredInt = -23;

        $this->assertEquals(-23, $int);

        IntHelper::ensureNegative($int);
    }

    public function testEnsureNegativeNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = array();

        IntHelper::ensureNegative($mayBeInt);
    }

    public function testEnsureNegativeNotNegative()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeNegativeInt = 25;

        IntHelper::ensureNegative($mayBeNegativeInt);
    }

    public function testEnsureBetween()
    {
        $int = 5;

        $ensuredBetween = &IntHelper::ensureBetween($int, 0, 6);

        $ensuredBetween = 4;

        $this->assertEquals(4, $int);
    }

    public function testEnsureBetweenNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = array();

        IntHelper::ensureBetween($mayBeInt, 0, 10);
    }

    public function testEnsureBetweenRangeErrorLess()
    {
        $this->setExpectedException('UnexpectedValueException');

        $int = 5;

        IntHelper::ensureBetween($int, 0, -3);
    }

    public function testEnsureBetweenRangeErrorEqual()
    {
        $this->setExpectedException('UnexpectedValueException');

        $int = 5;

        IntHelper::ensureBetween($int, 0, 0);
    }

    public function testEnsureBetweenLess()
    {
        $this->setExpectedException('OutOfRangeException');

        $int = 5;

        IntHelper::ensureBetween($int, 6, 10);
    }

    public function testEnsureBetweenLessOrEqual()
    {
        $this->setExpectedException('OutOfRangeException');

        $int = 5;

        IntHelper::ensureBetween($int, 5, 10);
    }

    public function testEnsureBetweenMore()
    {
        $this->setExpectedException('OutOfRangeException');

        $int = 5;

        IntHelper::ensureBetween($int, 0, 3);
    }

    public function testEnsureBetweenMoreOrEqual()
    {
        $this->setExpectedException('OutOfRangeException');

        $int = 5;

        IntHelper::ensureBetween($int, 0, 5);
    }

    public function testEnsureInRange()
    {
        $int = 5;

        $ensuredBetween = &IntHelper::ensureInRange($int, 0, 5);

        $ensuredBetween = 4;

        $this->assertEquals(4, $int);
    }

    public function testEnsureInRangeNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = array();

        IntHelper::ensureInRange($mayBeInt, 0, 10);
    }

    public function testEnsureInRangeRangeErrorLess()
    {
        $this->setExpectedException('UnexpectedValueException');

        $int = 5;

        IntHelper::ensureInRange($int, 0, -3);
    }

    public function testEnsureInRangeRangeEqual()
    {
        $int = 5;

        $ensuredInt = IntHelper::ensureInRange($int, 5, 5);

        $this->assertEquals(5, $ensuredInt);
    }

    public function testEnsureInRangeLess()
    {
        $this->setExpectedException('OutOfRangeException');

        $int = 5;

        IntHelper::ensureInRange($int, 6, 10);
    }

    public function testEnsureInRangeLessOrEqual()
    {
        $int = 5;

        $this->assertEquals(5, IntHelper::ensureInRange($int, 5, 10));
    }

    public function testEnsureInRangeMore()
    {
        $this->setExpectedException('OutOfRangeException');

        $int = 5;

        IntHelper::ensureInRange($int, 0, 3);
    }

    public function testEnsureInRangeMoreOrEqual()
    {
        $int = 5;

        $this->assertEquals(5, IntHelper::ensureInRange($int, 0, 5));
    }

    public function testEnsureZero()
    {
        $zero = 0;

        $ensuredZero = &IntHelper::ensureZero($zero);

        $ensuredZero = 23;

        $this->assertEquals(23, $zero);
    }

    public function testEnsureZeroNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = array();

        IntHelper::ensureZero($mayBeInt);
    }

    public function testEnsureZeroNotZero()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeZero = 25;

        IntHelper::ensureZero($mayBeZero);
    }

    public function testEnsureMoreThan()
    {
        $int = 0;

        $more = &IntHelper::ensureMoreThan($int, -20);

        $more = -3;

        $this->assertEquals(-3, $int);
    }

    public function testEnsureMoreThanNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = '25';

        IntHelper::ensureMoreThan($mayBeInt, 35);
    }

    public function testEnsureMoreThanAtLess()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeMore = 25;

        IntHelper::ensureMoreThan($mayBeMore, 35);
    }

    public function testEnsureMoreThanAtEquals()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeMore = 25;

        IntHelper::ensureMoreThan($mayBeMore, 25);
    }

    public function testEnsureMoreThanOrEquals()
    {
        $int = 0;

        $more = &IntHelper::ensureMoreThanOrEquals($int, -20);

        $more = -3;

        $this->assertEquals(-3, $int);

        $int = 0;

        $more = &IntHelper::ensureMoreThanOrEquals($int, 0);

        $more = -3;

        $this->assertEquals(-3, $int);
    }

    public function testEnsureMoreThanOrEqualsNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = '25';

        IntHelper::ensureMoreThanOrEquals($mayBeInt, 35);
    }

    public function testEnsureMoreThanOrEqualsAtLess()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = 33;

        IntHelper::ensureMoreThanOrEquals($mayBeInt, 35);
    }

    public function testEnsureLessThan()
    {
        $int = 0;

        $more = &IntHelper::ensureLessThan($int, 3);

        $more = 2;

        $this->assertEquals(2, $int);
    }

    public function testEnsureLessThanNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = '25';

        IntHelper::ensureLessThan($mayBeInt, 35);
    }

    public function testEnsureLessThanAtMore()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeLess = 25;

        IntHelper::ensureLessThan($mayBeLess, 20);
    }

    public function testEnsureLessThanAtEquals()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeLess = 25;

        IntHelper::ensureLessThan($mayBeLess, 25);
    }

    public function testEnsureLessThanOrEquals()
    {
        $int = 0;

        $more = &IntHelper::ensureLessThanOrEquals($int, 3);

        $more = 2;

        $this->assertEquals(2, $int);

        $int = 0;

        $more = &IntHelper::ensureLessThanOrEquals($int, 0);

        $more = 2;

        $this->assertEquals(2, $int);
    }

    public function testEnsureLessThanOrEqualsNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = '25';

        IntHelper::ensureLessThanOrEquals($mayBeInt, 35);
    }

    public function testEnsureLessThanOrEqualsAtMore()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = 33;

        IntHelper::ensureLessThanOrEquals($mayBeInt, 32);
    }

    public function testEnsureMoreThanZero()
    {
        $int = 2;

        $more = &IntHelper::ensureMoreThanZero($int);

        $more = 1;

        $this->assertEquals(1, $int);
    }

    public function testEnsureMoreThanZeroNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = '25';

        IntHelper::ensureMoreThanZero($mayBeInt);
    }

    public function testEnsureMoreThanZeroAtZero()
    {
        $this->setExpectedException('UnexpectedValueException');

        $zero = 0;

        IntHelper::ensureMoreThanZero($zero);
    }

    public function testEnsureMoreThanZeroAtNegative()
    {
        $this->setExpectedException('UnexpectedValueException');

        $negative = -1;

        IntHelper::ensureMoreThanZero($negative);
    }

    public function testEnsureMoreThanOrEqualsZero()
    {
        $int = 2;

        $more = &IntHelper::ensureMoreThanOrEqualsZero($int);

        $more = 1;

        $this->assertEquals(1, $int);
    }

    public function testEnsureMoreThanOrEqualsZeroNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = '25';

        IntHelper::ensureMoreThanOrEqualsZero($mayBeInt);
    }

    public function testEnsureMoreThanOrEqualsZeroAtZero()
    {
        $int = 0;

        $this->assertEquals(0, IntHelper::ensureMoreThanOrEqualsZero($int));
    }

    public function testEnsureMoreThanOrEqualsZeroAtNegative()
    {
        $this->setExpectedException('UnexpectedValueException');

        $negative = -1;

        IntHelper::ensureMoreThanOrEqualsZero($negative);
    }

    public function testEnsureLessThanZero()
    {
        $int = -2;

        $less = &IntHelper::ensureLessThanZero($int);

        $less = -1;

        $this->assertEquals(-1, $int);
    }

    public function testEnsureLessThanZeroNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = '25';

        IntHelper::ensureLessThanZero($mayBeInt);
    }

    public function testEnsureLessThanZeroAtZero()
    {
        $this->setExpectedException('UnexpectedValueException');

        $zero = 0;

        IntHelper::ensureLessThanZero($zero);
    }

    public function testEnsureLessThanZeroAtPositive()
    {
        $this->setExpectedException('UnexpectedValueException');

        $positive = -1;

        IntHelper::ensureMoreThanZero($positive);
    }

    public function testEnsureLessThanOrEqualsZero()
    {
        $int = -2;

        $less = &IntHelper::ensureLessThanOrEqualsZero($int);

        $less = -1;

        $this->assertEquals(-1, $int);
    }

    public function testEnsureLessThanOrEqualsZeroNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeInt = '25';

        IntHelper::ensureLessThanOrEqualsZero($mayBeInt);
    }

    public function testEnsureLessThanOrEqualsZeroAtZero()
    {
        $int = 0;

        $this->assertEquals(0, IntHelper::ensureLessThanOrEqualsZero($int));
    }

    public function testEnsureLessThanOrEqualsZeroAtPositive()
    {
        $this->setExpectedException('UnexpectedValueException');

        $positive = 1;

        IntHelper::ensureLessThanOrEqualsZero($positive);
    }

}