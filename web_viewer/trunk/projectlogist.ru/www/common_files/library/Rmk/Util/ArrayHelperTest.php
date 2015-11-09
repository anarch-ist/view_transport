<?php
/**
 * Автотест помощника для работы с массивами.
 *
 * @category Rmk
 * @package  Rmk_Util
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Util;

use Rmk\Util\ArrayHelper,
    PHPUnit_Framework_TestCase,
    stdClass;

class ArrayHelperTest extends PHPUnit_Framework_TestCase
{

    public function testIsArray()
    {
        $array = array();
        $int     = 0;
        $boolean = false;
        $string  = 'str';

        $this->assertTrue(ArrayHelper::isArray($array));
        $this->assertFalse(ArrayHelper::isArray($int));
        $this->assertFalse(ArrayHelper::isArray($boolean));
        $this->assertFalse(ArrayHelper::isArray($string));
    }

    public function testIsList()
    {
        $list = array(
            'v1',
            'v2',
            'v3'
        );

        $hash = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $brokenList = array(
            'v1',
            'v2',
            4 => 'v3'
        );

        $this->assertTrue(ArrayHelper::isList($list));
        $this->assertFalse(ArrayHelper::isList($hash));
        $this->assertFalse(ArrayHelper::isList($brokenList));
    }

    public function testIsHash()
    {
        $list = array(
            'v1',
            'v2',
            'v3'
        );

        $hash = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $brokenList = array(
            'v1',
            'v2',
            4 => 'v3'
        );

        $this->assertTrue(ArrayHelper::isHash($hash));
        $this->assertTrue(ArrayHelper::isHash($brokenList));
        $this->assertFalse(ArrayHelper::isHash($list));
    }

    public function testEnsureArray()
    {
        $array = array(
            'k1' => 'v1'
        );

        $ensuredArray = &ArrayHelper::ensureArray($array);

        $ensuredArray['k1'] = 'v11';

        $this->assertEquals('v11', $array['k1']);

        ArrayHelper::ensureArray($array);

        $this->setExpectedException('UnexpectedValueException');

        $mayBeArray = 23;

        ArrayHelper::ensureArray($mayBeArray);
    }

    public function testEnsureList()
    {
        $array = array(
            'v1'
        );

        $ensuredArray = &ArrayHelper::ensureList($array);
        ArrayHelper::ensureList($array);

        $ensuredArray[0] = 'v11';

        $this->assertEquals('v11', $array[0]);
    }

    public function testEnsureListNotArray()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeArray = 23;

        ArrayHelper::ensureList($mayBeArray);
    }

    public function testEnsureListNotList()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeList = array(
            'k1' => 'v1'
        );

        ArrayHelper::ensureList($mayBeList);
    }

    public function testEnsureHash()
    {
        $array = array(
            'k1' => 'v1'
        );

        $ensuredArray = &ArrayHelper::ensureHash($array);
        ArrayHelper::ensureHash($array);

        $ensuredArray['k1'] = 'v11';

        $this->assertEquals('v11', $array['k1']);
    }

    public function testEnsureHashNotArray()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeArray = 23;

        ArrayHelper::ensureHash($mayBeArray);
    }

    public function testEnsureHashNotHash()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeHash = array(
            'v1'
        );

        ArrayHelper::ensureHash($mayBeHash);
    }

    public function testEnsureEmpty()
    {
        $emptyArray = array(
        );

        $ensuredEmptyArray = &ArrayHelper::ensureEmpty($emptyArray);

        $ensuredEmptyArray[0] = '23234';

        $this->assertEquals('23234', $emptyArray[0]);
    }

    public function testEnsureEmptyNotArray()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeArray = 23;

        ArrayHelper::ensureEmpty($mayBeArray);
    }

    public function testEnsureEmptyNotEmpty()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeEmptyArray = array(
            'v1'
        );

        ArrayHelper::ensureEmpty($mayBeEmptyArray);
    }

    public function testEnsureNotEmpty()
    {
        $notEmptyArray = array(
            0 => '254123'
        );

        $ensuredNotEmptyArray = &ArrayHelper::ensureNotEmpty($notEmptyArray);

        $ensuredNotEmptyArray[0] = '23234';

        $this->assertEquals('23234', $notEmptyArray[0]);
    }

    public function testEnsureNotEmptyNotArray()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeArray = 23;

        ArrayHelper::ensureNotEmpty($mayBeArray);
    }

    public function testEnsureNotEmptyEmpty()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeNotEmptyArray = array(
        );

        ArrayHelper::ensureNotEmpty($mayBeNotEmptyArray);
    }

    public function testEnsureContainsRange()
    {
        $array = array(
            '1',
            '2',
            '3'
        );

        $range = ArrayHelper::ensureContainsRange($array, 0, 2);

        $this->assertEquals(array('1', '2', '3'), $range);

        $range[3] = '4';

        $this->assertEquals(array('1', '2', '3', '4'), $range);
    }

    public function testEnsureContainsRangeAtFromNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            '1'
        );

        ArrayHelper::ensureContainsRange($array, '0', 2);
    }

    public function testEnsureContainsRangeAtToNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            '1'
        );

        ArrayHelper::ensureContainsRange($array, 0, '2');
    }

    public function testEnsureContainsRangeAtNegativeFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            '1'
        );

        ArrayHelper::ensureContainsRange($array, -1, 2);
    }

    public function testEnsureContainsRangeAtToLessThanFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            '1'
        );

        ArrayHelper::ensureContainsRange($array, 2, 0);
    }

    public function testEnsureContainsRangeAtToEqualsFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            '1'
        );

        ArrayHelper::ensureContainsRange($array, 0, 0);
    }

    public function testEnsureContainsRangeAtToMoreThanCount()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            '1'
        );

        ArrayHelper::ensureContainsRange($array, 0, 2);
    }

    public function testEnsureContainsListRange()
    {
        $array = array(
            '1',
            '2',
            '3'
        );

        $range = ArrayHelper::ensureContainsListRange($array, 0, 2);

        $this->assertEquals(array('1', '2', '3'), $range);

        $range[3] = '4';

        $this->assertEquals(array('1', '2', '3', '4'), $range);
    }

    public function testEnsureContainsListRangeAtNotList()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'k1' => '1'
        );

        ArrayHelper::ensureContainsListRange($array, 0, 2);
    }

    public function testEnsureContainsListRangeAtFromNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            '1'
        );

        ArrayHelper::ensureContainsListRange($array, '0', 2);
    }

    public function testEnsureContainsListRangeAtToNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            '1'
        );

        ArrayHelper::ensureContainsListRange($array, 0, '2');
    }

    public function testEnsureContainsListRangeAtNegativeFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            '1'
        );

        ArrayHelper::ensureContainsListRange($array, -1, 2);
    }

    public function testEnsureContainsListRangeAtToLessThanFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            '1'
        );

        ArrayHelper::ensureContainsListRange($array, 2, 0);
    }

    public function testEnsureContainsListRangeAtToEqualsFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            '1'
        );

        ArrayHelper::ensureContainsListRange($array, 0, 0);
    }

    public function testEnsureContainsListRangeAtToMoreThanCount()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            '1'
        );

        ArrayHelper::ensureContainsListRange($array, 0, 2);
    }

    public function testClear()
    {
        $array = array(
            '1',
            '2',
            '3'
        );

        ArrayHelper::clear($array);

        $this->assertEquals(array(), $array);
        $this->assertTrue(empty($array));
    }

    public function testContains()
    {
        $array1 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5'
        );

        $array2 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $array3 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5',
            'k6' => 'v6'
        );

        $array4 = array(
            'k1'  => 'v1',
            'k2'  => 'v2',
            'k88' => 'v88'
        );

        $array5 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v4'
        );

        $this->assertTrue(ArrayHelper::contains($array1, $array2));
        $this->assertFalse(ArrayHelper::contains($array1, $array3));
        $this->assertFalse(ArrayHelper::contains($array1, $array4));
        $this->assertFalse(ArrayHelper::contains($array1, $array5));
    }

    public function testsContainsValues()
    {
        $array1 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5'
        );

        $array2 = array(
            'v1',
            'v2',
            'v3'
        );

        $array3 = array(
            'v1',
            'v2',
            'v88'
        );

        $array4 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5',
            'k6' => 'v6'
        );

        $this->assertTrue(ArrayHelper::containsValues($array1, $array2));
        $this->assertFalse(ArrayHelper::containsValues($array1, $array3));
        $this->assertFalse(ArrayHelper::containsValues($array1, $array4));
    }

    public function testContainsKeys()
    {
        $array1 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5'
        );

        $array2 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $array3 = array(
            'k1'  => 'v1',
            'k2'  => 'v2',
            'k88' => 'v88'
        );

        $array4 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5',
            'k6' => 'v6'
        );

        $this->assertTrue(ArrayHelper::containsKeys($array1, $array2));
        $this->assertFalse(ArrayHelper::containsKeys($array1, $array3));
        $this->assertFalse(ArrayHelper::containsKeys($array1, $array4));
    }

    public function testContainsKeysList()
    {
        $array1 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5'
        );

        $array2 = array(
            'k1',
            'k2',
            'k3'
        );

        $array3 = array(
            'k1',
            'k2',
            'k88'
        );

        $array4 = array(
            'k1',
            'k2',
            'k3',
            'k4',
            'k5',
            'k6'
        );

        $this->assertTrue(ArrayHelper::containsKeysList($array1, $array2));
        $this->assertFalse(ArrayHelper::containsKeysList($array1, $array3));
        $this->assertFalse(ArrayHelper::containsKeysList($array1, $array4));
    }

    public function testContainsRange()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->assertTrue(ArrayHelper::containsRange($array, 0, 1));
        $this->assertTrue(ArrayHelper::containsRange($array, 1, 2));
        $this->assertTrue(ArrayHelper::containsRange($array, 0, 2));

        $this->assertFalse(ArrayHelper::containsRange($array, 0, 4));
    }

    public function testContainsRangeAtFromNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'k1' => 'v1'
        );

        ArrayHelper::containsRange($array, '0', 2);
    }

    public function testContainsRangeAtToNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'k1' => 'v1'
        );

        ArrayHelper::containsRange($array, 0, '2');
    }

    public function testContainsRangeAtFromLessThanZero()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'k1' => 'v1'
        );

        ArrayHelper::containsRange($array, -1, 2);
    }

    public function testContainsRangeAtToLessThanFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'k1' => 'v1'
        );

        ArrayHelper::containsRange($array, 0, -1);
    }

    public function testContainsRangeAtTEqualsFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'k1' => 'v1'
        );

        ArrayHelper::containsRange($array, 0, 0);
    }

    public function testContainsListRange()
    {
        $array = array(
            'v1',
            'v2',
            'v3'
        );

        $hashArray = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->assertTrue(ArrayHelper::containsListRange($array, 0, 1));
        $this->assertTrue(ArrayHelper::containsListRange($array, 1, 2));
        $this->assertTrue(ArrayHelper::containsListRange($array, 0, 2));

        $this->assertFalse(ArrayHelper::containsListRange($array, 0, 4));
        $this->assertFalse(ArrayHelper::containsListRange($hashArray, 0, 2));
    }

    public function testContainsListRangeAtFromNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'v1'
        );

        ArrayHelper::containsListRange($array, '0', 2);
    }

    public function testContainsListRangeAtToNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'v1'
        );

        ArrayHelper::containsListRange($array, 0, '2');
    }

    public function testContainsListRangeAtFromLessThanZero()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'v1'
        );

        ArrayHelper::containsListRange($array, -1, 2);
    }

    public function testContainsListRangeAtToLessThanFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'v1'
        );

        ArrayHelper::containsListRange($array, 0, -1);
    }

    public function testContainsListRangeAtTEqualsFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'v1'
        );

        ArrayHelper::containsListRange($array, 0, 0);
    }

    public function testEquals()
    {
        $array1 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5'
        );

        $array2 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5'
        );

        $array3 = array(
            'k5' => 'v5',
            'k1' => 'v1',
            'k3' => 'v3',
            'k2' => 'v2',
            'k4' => 'v4'
        );

        $array4 = array(
            'k6' => 'v5',
            'k1' => 'v1',
            'k3' => 'v3',
            'k2' => 'v2',
            'k4' => 'v4'
        );

        $array5 = array(
            'k5' => 'v5',
            'k1' => 'v1',
            'k3' => 'v22',
            'k2' => 'v2',
            'k4' => 'v4'
        );

        $this->assertTrue(ArrayHelper::equals($array1, $array2));
        $this->assertFalse(ArrayHelper::equals($array1, $array3));
        $this->assertFalse(ArrayHelper::equals($array1, $array4));
        $this->assertFalse(ArrayHelper::equals($array1, $array5));
    }

    public function testEqualsValues()
    {
        $array1 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5'
        );

        $array2 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5'
        );

        $array3 = array(
            'k5' => 'v5',
            'k1' => 'v1',
            'k3' => 'v3',
            'k2' => 'v2',
            'k4' => 'v4'
        );

        $array4 = array(
            'k6' => 'v5',
            'k1' => 'v1',
            'k3' => 'v3',
            'k2' => 'v2',
            'k4' => 'v4'
        );

        $array5 = array(
            'k5' => 'v5',
            'k1' => 'v1',
            'k3' => 'v22',
            'k2' => 'v2',
            'k4' => 'v4'
        );

        $this->assertTrue(ArrayHelper::equalsValues($array1, $array2));
        $this->assertFalse(ArrayHelper::equalsValues($array1, $array3));
        $this->assertFalse(ArrayHelper::equalsValues($array1, $array4));
        $this->assertFalse(ArrayHelper::equalsValues($array1, $array5));
    }

    public function testEqualsKeys()
    {
        $array1 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5'
        );

        $array2 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5'
        );

        $array3 = array(
            'k5' => 'v5',
            'k1' => 'v1',
            'k3' => 'v3',
            'k2' => 'v2',
            'k4' => 'v4'
        );

        $array4 = array(
            'k6' => 'v5',
            'k1' => 'v1',
            'k3' => 'v3',
            'k2' => 'v2',
            'k4' => 'v4'
        );

        $array5 = array(
            'k5' => 'v5',
            'k1' => 'v1',
            'k3' => 'v22',
            'k2' => 'v2',
            'k4' => 'v4'
        );

        $this->assertTrue(ArrayHelper::equalsKeys($array1, $array2));
        $this->assertFalse(ArrayHelper::equalsKeys($array1, $array3));
        $this->assertFalse(ArrayHelper::equalsKeys($array1, $array4));
        $this->assertFalse(ArrayHelper::equalsKeys($array1, $array5));
    }

    public function testEqualsKeysList()
    {
        $array1 = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5'
        );

        $array2 = array(
            'k1',
            'k2',
            'k3',
            'k4',
            'k5'
        );

        $array3 = array(
            'k5',
            'k1',
            'k3',
            'k2',
            'k4'
        );

        $array4 = array(
            'k6',
            'k1',
            'k3',
            'k2',
            'k4'
        );

        $array5 = array(
            'k5',
            'k1',
            'k3',
            'k2',
            'k4'
        );

        $this->assertTrue(ArrayHelper::equalsKeysList($array1, $array2));
        $this->assertFalse(ArrayHelper::equalsKeysList($array1, $array3));
        $this->assertFalse(ArrayHelper::equalsKeysList($array1, $array4));
        $this->assertFalse(ArrayHelper::equalsKeysList($array1, $array5));
    }

    public function testReplace()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
        );

        $replacement = array(
            'k1' => 'v11',
            'k2' => 'v22',
            'k3' => 'v33',
            'k4' => 'v44'
        );

        ArrayHelper::replace($array, $replacement);

        $this->assertEquals('v11', $array['k1']);
        $this->assertEquals('v22', $array['k2']);
        $this->assertEquals('v33', $array['k3']);
        $this->assertEquals('v44', $array['k4']);
    }

    public function testMerge()
    {
        $array = array(
            'color' => 'red',
            2,
            4
        );

        $array2 = array(
            'a',
            'b',
            'color' => 'green',
            'shape' => 'trapezoid',
            4
        );

        ArrayHelper::merge($array, $array2);

        $this->assertEquals('green', $array['color']);
        $this->assertEquals(2, $array[0]);
        $this->assertEquals(4, $array[1]);
        $this->assertEquals('a', $array[2]);
        $this->assertEquals('b', $array[3]);
        $this->assertEquals('trapezoid', $array['shape']);
        $this->assertEquals(4, $array[4]);
    }

    public function testMergeUnique()
    {
        $array = array(
            1,
            2,
            3
        );

        $array2 = array(
            1,
            2,
            3
        );

        ArrayHelper::mergeUnique($array, $array2);

        $this->assertEquals(1, $array[0]);
        $this->assertEquals(2, $array[1]);
        $this->assertEquals(3, $array[2]);
        $this->assertEquals(3, ArrayHelper::count($array));
    }

    public function testIsEmpty()
    {
        $array = array(
            '1',
            '2',
            '3'
        );

        $emptyArray = array(
        );

        $this->assertFalse(ArrayHelper::isEmpty($array));
        $this->assertTrue(ArrayHelper::isEmpty($emptyArray));
    }

    public function testIsNotEmpty()
    {
        $array = array(
            '1',
            '2',
            '3'
        );

        $emptyArray = array(
        );

        $this->assertTrue(ArrayHelper::isNotEmpty($array));
        $this->assertFalse(ArrayHelper::isNotEmpty($emptyArray));
    }

    public function testEnsureContainsKey()
    {
        $array = array(
            '1',
            '2',
            '3'
        );

        $ensuredArray = &ArrayHelper::ensureContainsKey($array, 0);

        $ensuredArray[0] = '11';

        $this->assertEquals(array('11', '2', '3'), $array);
    }

    public function testEnsureContainsKeyAtNotArray()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeArray = 'sadasdasd';

        ArrayHelper::ensureContainsKey($mayBeArray, 0);
    }

    public function testEnsureContainsKeyAtNotContains()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            '1',
            '2',
            '3'
        );

        ArrayHelper::ensureContainsKey($array, 'k1');
    }

    public function testEnsureKeyInArray()
    {
        $key   = 0;
        $array = array(
            '1',
            '2',
            '3'
        );

        $ensuredKey = &ArrayHelper::ensureKeyInArray($array, $key);

        $ensuredKey = 2;

        $this->assertEquals(2, $key);
    }

    public function testEnsureKeyInArrayAtNotArray()
    {
        $this->setExpectedException('UnexpectedValueException');

        $key        = '233';
        $mayBeArray = 'sadasdasd';

        ArrayHelper::ensureKeyInArray($mayBeArray, $key);
    }

    public function testEnsureKeyInArrayAtNotContains()
    {
        $this->setExpectedException('UnexpectedValueException');

        $key   = '233';
        $array = array(
            '1',
            '2',
            '3'
        );

        ArrayHelper::ensureKeyInArray($array, $key);
    }

    public function testCount()
    {
        $array = array(
            '1',
            '2',
            '3'
        );

        $this->assertEquals(3, ArrayHelper::count($array));

        ArrayHelper::clear($array);

        $this->assertEquals(0, ArrayHelper::count($array));
    }

    public function testSet()
    {
        $array = array();

        ArrayHelper::set($array, 'k1', 'v1');

        $this->assertEquals('v1', $array['k1']);
    }

    public function testSetIfNull()
    {
        $array = array();

        ArrayHelper::setIfNull($array, 'k1', 'v1');

        $this->assertEquals('v1', $array['k1']);
    }

    public function testSetIfNullException()
    {
        $array = array();

        ArrayHelper::setIfNull($array, 'k1', 'v1');

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::setIfNull($array, 'k1', 'v1');
    }

    public function testSetIfNotNull()
    {
        $array = array(
            'k1' => 'v1'
        );

        ArrayHelper::setIfNotNull($array, 'k1', 'v2');

        $this->assertEquals('v2', $array['k1']);

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::setIfNotNull($array, 'k2', 'v2');
    }

    public function testSetObject()
    {
        $array = array(
        );

        $object = new stdClass();

        ArrayHelper::setObject($array, 'key1', $object, 'stdClass');

        $this->assertEquals($object, $array['key1']);
    }

    public function testSetObjectUnexpectedValueException()
    {
        $array = array(
        );

        $object = new stdClass();

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::setObject($array, 'key1', $object, 'RuntimeException');
    }

    public function testSetObjectIfNull()
    {
        $array = array(
        );

        $object = new stdClass();

        ArrayHelper::setObjectIfNull($array, 'key1', $object, 'stdClass');

        $this->assertEquals($object, $array['key1']);

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::setObjectIfNull($array, 'key1', $object, 'stdClass');
    }

    public function testSetObjectInNullUnexpectedValueException()
    {
        $array = array(
        );

        $object = new stdClass();

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::setObjectIfNull(
                $array, 'key1', $object, 'RuntimeException'
        );
    }

    public function testSetObjectIfNotNull()
    {
        $object1 = new stdClass();
        $object2 = new stdClass();

        $array = array(
            'k1' => $object1
        );

        ArrayHelper::setObjectIfNotNull($array, 'k1', $object2, 'stdClass');

        $this->assertEquals($object2, $array['k1']);

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::setObjectIfNotNull($array, 'k2', $object1, 'stdClass');
    }

    public function testSetObjectIfNotNullUnexpectedValueException()
    {
        $this->setExpectedException('UnexpectedValueException');

        $object = new stdClass();

        $array = array(
            'k1' => $object
        );

        ArrayHelper::setObjectIfNotNull($array, 'k1', $object, 'Exception');
    }

    public function testSetReference()
    {
        $array = array(
        );

        $value = 'v1';

        ArrayHelper::setReference($array, 'k1', $value);

        $this->assertEquals('v1', $array['k1']);

        $value = 'v2';

        $this->assertEquals('v2', $array['k1']);
    }

    public function testSetReferenceIfNull()
    {
        $array = array(
        );

        $value = 'v1';

        ArrayHelper::setReferenceIfNull($array, 'k1', $value);

        $this->assertEquals('v1', $array['k1']);

        $value = 'v2';

        $this->assertEquals('v2', $array['k1']);

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::setReferenceIfNull($array, 'k1', $value);
    }

    public function testSetReferenceIfNotNull()
    {
        $array = array(
            'k1' => 'v1'
        );

        $value = 'v2';

        ArrayHelper::setReferenceIfNotNull($array, 'k1', $value);

        $this->assertEquals('v2', $array['k1']);

        $value = 'v3';

        $this->assertEquals('v3', $array['k1']);

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::setReferenceIfNotNull($array, 'k2', $value);
    }

    public function testAdd()
    {
        $array = array();

        ArrayHelper::add($array, 'v1');
        ArrayHelper::add($array, 'v2');
        ArrayHelper::add($array, 'v3');

        $this->assertEquals('v1', $array[0]);
        $this->assertEquals('v2', $array[1]);
        $this->assertEquals('v3', $array[2]);
    }

    public function testAddFirst()
    {
        $array = array();

        ArrayHelper::addFirst($array, 'v1');
        ArrayHelper::addFirst($array, 'v2');
        ArrayHelper::addFirst($array, 'v3');

        $this->assertEquals('v3', $array[0]);
        $this->assertEquals('v2', $array[1]);
        $this->assertEquals('v1', $array[2]);
    }

    public function testAddLast()
    {
        $array = array();

        ArrayHelper::addLast($array, 'v1');
        ArrayHelper::addLast($array, 'v2');
        ArrayHelper::addLast($array, 'v3');

        $this->assertEquals('v1', $array[0]);
        $this->assertEquals('v2', $array[1]);
        $this->assertEquals('v3', $array[2]);
    }

    public function testAddObject()
    {
        $array = array();

        $object1 = new stdClass();
        $object2 = new stdClass();
        $object3 = new stdClass();

        ArrayHelper::addObject($array, $object1, 'stdClass');
        ArrayHelper::addObject($array, $object2, 'stdClass');
        ArrayHelper::addObject($array, $object3, 'stdClass');

        $this->assertEquals($object1, $array[0]);
        $this->assertEquals($object2, $array[1]);
        $this->assertEquals($object3, $array[2]);

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::addObject($array, $object1, 'RuntimeException');
    }

    public function testAddFirstObject()
    {
        $array = array();

        $object1 = new stdClass();
        $object2 = new stdClass();
        $object3 = new stdClass();

        ArrayHelper::addFirstObject($array, $object3, 'stdClass');
        ArrayHelper::addFirstObject($array, $object2, 'stdClass');
        ArrayHelper::addFirstObject($array, $object1, 'stdClass');

        $this->assertEquals($object1, $array[0]);
        $this->assertEquals($object2, $array[1]);
        $this->assertEquals($object3, $array[2]);

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::addFirstObject($array, $object1, 'RuntimeException');
    }

    public function testAddLastObject()
    {
        $array = array();

        $object1 = new stdClass();
        $object2 = new stdClass();
        $object3 = new stdClass();

        ArrayHelper::addLastObject($array, $object1, 'stdClass');
        ArrayHelper::addLastObject($array, $object2, 'stdClass');
        ArrayHelper::addLastObject($array, $object3, 'stdClass');

        $this->assertEquals($object1, $array[0]);
        $this->assertEquals($object2, $array[1]);
        $this->assertEquals($object3, $array[2]);

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::addLastObject($array, $object1, 'RuntimeException');
    }

    public function testInsertIntoList()
    {
        $list = array(
            'v1',
            'v2',
            'v3'
        );

        ArrayHelper::insertIntoList($list, 0, 'v0');

        $this->assertEquals(array('v0', 'v1', 'v2', 'v3'), $list);

        ArrayHelper::insertIntoList($list, 4, 'v4');

        $this->assertEquals(array('v0', 'v1', 'v2', 'v3', 'v4'), $list);

        ArrayHelper::insertIntoList($list, 1, 'v0.1');
        ArrayHelper::insertIntoList($list, 2, 'v0.2');

        $this->assertEquals(array('v0', 'v0.1', 'v0.2', 'v1', 'v2', 'v3', 'v4'),
                            $list);
    }

    public function testInsertIntoListAtEmptyList()
    {
        $list = array(
        );

        ArrayHelper::insertIntoList($list, 0, 'v0');

        $this->assertEquals(array('v0'), $list);
    }

    public function testInsertIntoListAtIndexNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $list = array(
        );

        ArrayHelper::insertIntoList($list, '0', 'v0');
    }

    public function testInsertIntoListAtIndexLessThanZero()
    {
        $this->setExpectedException('UnexpectedValueException');

        $list = array(
        );

        ArrayHelper::insertIntoList($list, -1, 'v0');
    }

    public function testInsertIntoListAtIndexMoreThanCount()
    {
        $this->setExpectedException('UnexpectedValueException');

        $list = array(
            'v1',
            'v2',
            'v3'
        );

        ArrayHelper::insertIntoList($list, 4, 'v0');
    }

    public function testInsertObjectIntoList()
    {
        $object0  = new stdClass();
        $object01 = new stdClass();
        $object02 = new stdClass();
        $object1  = new stdClass();
        $object2  = new stdClass();
        $object3  = new stdClass();
        $object4  = new stdClass();

        $list = array(
            $object1,
            $object2,
            $object3
        );

        ArrayHelper::insertObjectIntoList($list, 0, $object0, 'stdClass');

        $this->assertEquals(array($object0, $object1, $object2, $object3), $list);

        ArrayHelper::insertObjectIntoList($list, 4, $object4, 'stdClass');

        $this->assertEquals(array($object0, $object1, $object2, $object3, $object4),
                            $list);

        ArrayHelper::insertObjectIntoList($list, 1, $object01, 'stdClass');
        ArrayHelper::insertObjectIntoList($list, 2, $object02, 'stdClass');

        $this->assertEquals(array($object0, $object01, $object02, $object1, $object2, $object3, $object4),
                            $list);
    }

    public function testInsertObjectIntoListAtEmptyList()
    {
        $object = new stdClass();

        $list = array(
        );

        ArrayHelper::insertObjectIntoList($list, 0, $object, 'stdClass');

        $this->assertEquals(array($object), $list);
    }

    public function testInsertObjectIntoListAtErrorType()
    {
        $this->setExpectedException('UnexpectedValueException');

        $object = new stdClass();

        $list = array(
        );

        ArrayHelper::insertObjectIntoList($list, 0, $object, 'Exception');
    }

    public function testInsertObjectIntoListAtIndexNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $object = new stdClass();

        $list = array(
        );

        ArrayHelper::insertObjectIntoList($list, '0', $object, 'stdClass');
    }

    public function testInsertObjectIntoListAtIndexLessThanZero()
    {
        $this->setExpectedException('UnexpectedValueException');

        $object = new stdClass();

        $list = array(
        );

        ArrayHelper::insertObjectIntoList($list, -1, $object, 'stdClass');
    }

    public function testInsertObjectIntoListAtIndexMoreThanCount()
    {
        $this->setExpectedException('UnexpectedValueException');

        $object = new stdClass();

        $list = array(
            new stdClass(),
            new stdClass(),
            new stdClass()
        );

        ArrayHelper::insertObjectIntoList($list, 4, $object, 'stdClass');
    }

    public function testGetByKey()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $v1   = ArrayHelper::getByKey($array, 'k1');
        $v2   = ArrayHelper::getByKey($array, 'k2');
        $v3   = ArrayHelper::getByKey($array, 'k3');
        $null = ArrayHelper::getByKey($array, 'k25');

        $this->assertEquals('v1', $v1);
        $this->assertEquals('v2', $v2);
        $this->assertEquals('v3', $v3);
        $this->assertNull($null);
    }

    public function testGetNotNullByKey()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $v1 = ArrayHelper::getNotNullByKey($array, 'k1');
        $v2 = ArrayHelper::getNotNullByKey($array, 'k2');
        $v3 = ArrayHelper::getNotNullByKey($array, 'k3');

        $this->assertEquals('v1', $v1);
        $this->assertEquals('v2', $v2);
        $this->assertEquals('v3', $v3);
    }

    public function testGetNotNullByKeyException()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->setExpectedException('UnexpectedValueException');

        $v1 = ArrayHelper::getNotNullByKey($array, 'k25');
    }

    public function testGetByKeyOrDefault()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $v1      = ArrayHelper::getByKeyOrDefault($array, 'k1');
        $v2      = ArrayHelper::getByKeyOrDefault($array, 'k2', 'abc');
        $default = ArrayHelper::getByKeyOrDefault($array, 'k23', 'default');

        $this->assertEquals('v1', $v1);
        $this->assertEquals('v2', $v2);
        $this->assertEquals('default', $default);
    }

    public function testGetReferenceByKey()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $v1Reference = &ArrayHelper::getReferenceByKey($array, 'k1');

        $v1Reference = 'v11';

        $this->assertEquals('v11', $array['k1']);
    }

    public function testGetNotNullReferenceByKey()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $v1Reference = &ArrayHelper::getNotNullReferenceByKey($array, 'k1');

        $v1Reference = 'v11';

        $this->assertEquals('v11', $array['k1']);

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::getNotNullReferenceByKey($array, 'k4');
    }

    public function testGetReferenceByKeyOrDefault()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $default = 'def';

        $v1Reference = &ArrayHelper::getReferenceByKeyOrDefault($array, 'k1');
        $v1Reference = 'v11';

        $this->assertEquals('v11', $array['k1']);

        $v2Reference = &ArrayHelper::getReferenceByKeyOrDefault($array, 'k2',
                                                                $default);
        $this->assertEquals('v2', $v2Reference);

        $v2Reference = 'v22';
        $this->assertEquals('v22', $array['k2']);

        $defaultReference = &ArrayHelper::getReferenceByKeyOrDefault($array,
                                                                     'k4',
                                                                     $default);
        $this->assertEquals('def', $defaultReference);

        $defaultReference = 'defC';
        $this->assertEquals('defC', $default);

        $defaultNull = &ArrayHelper::getReferenceByKeyOrDefault($array, 'k4');
        $this->assertNull($defaultNull);
    }

    public function testGetAt()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $v1    = ArrayHelper::getAt($array, 0);
        $v2    = ArrayHelper::getAt($array, 1);
        $v3    = ArrayHelper::getAt($array, 2);
        $null1 = ArrayHelper::getAt($array, 25);
        $null2 = ArrayHelper::getAt($array, -1);

        $this->assertEquals('v1', $v1);
        $this->assertEquals('v2', $v2);
        $this->assertEquals('v3', $v3);
        $this->assertNull($null1);
        $this->assertNull($null2);
    }

    public function testGetKeyAt()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $k1    = ArrayHelper::getKeyAt($array, 0);
        $k2    = ArrayHelper::getKeyAt($array, 1);
        $k3    = ArrayHelper::getKeyAt($array, 2);
        $null1 = ArrayHelper::getKeyAt($array, 25);
        $null2 = ArrayHelper::getKeyAt($array, -25);

        $this->assertEquals('k1', $k1);
        $this->assertEquals('k2', $k2);
        $this->assertEquals('k3', $k3);
        $this->assertNull($null1);
        $this->assertNull($null2);
    }

    public function testGetNotNullAt()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->assertEquals('v1', ArrayHelper::getNotNullAt($array, 0));
        $this->assertEquals('v2', ArrayHelper::getNotNullAt($array, 1));
        $this->assertEquals('v3', ArrayHelper::getNotNullAt($array, 2));
    }

    public function testGetNotNullAtEmpty()
    {
        $array = array(
        );

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::getNotNullAt($array, 0);
    }

    public function testGetNotNullAtNegativeRange()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::getNotNullAt($array, -1);
    }

    public function testGetNotNullAtPositiveRange()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::getNotNullAt($array, 3);
    }

    public function testGetNotNullKeyAt()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->assertEquals('k1', ArrayHelper::getNotNullKeyAt($array, 0));
        $this->assertEquals('k2', ArrayHelper::getNotNullKeyAt($array, 1));
        $this->assertEquals('k3', ArrayHelper::getNotNullKeyAt($array, 2));
    }

    public function testGetNotNullKeyAtEmpty()
    {
        $array = array(
        );

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::getNotNullKeyAt($array, 0);
    }

    public function testGetNotNullKeyAtNegativeRange()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::getNotNullKeyAt($array, -1);
    }

    public function testGetNotNullKeyAtPositiveRange()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::getNotNullKeyAt($array, 3);
    }

    public function testGetFirst()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $first = ArrayHelper::getFirst($array);

        $this->assertEquals('v1', $first);
    }

    public function testGetFirstOnEmpty()
    {
        $array = array(
        );

        $first = ArrayHelper::getFirst($array);

        $this->assertNull($first);
    }

    public function testGetFirstKey()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $firstKey = ArrayHelper::getFirstKey($array);

        $this->assertEquals('k1', $firstKey);
    }

    public function testGetNotNullFirst()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $first = ArrayHelper::getNotNullFirst($array);

        $this->assertEquals('v1', $first);
    }

    public function testGetNotNullFirstOnEmpty()
    {
        $array = array(
        );

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::getNotNullFirst($array);
    }

    public function testGetNotNullFirstKey()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $firstKey = ArrayHelper::getNotNullFirstKey($array);

        $this->assertEquals('k1', $firstKey);
    }

    public function testGetNotNullFirstKeyOnEmpty()
    {
        $array = array(
        );

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::getNotNullFirstKey($array);
    }

    public function testGetLast()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $last = ArrayHelper::getLast($array);

        $this->assertEquals('v3', $last);
    }

    public function testGetLastOnEmpty()
    {
        $array = array(
        );

        $last = ArrayHelper::getLast($array);

        $this->assertNull($last);
    }

    public function testGetLastKey()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $lastKey = ArrayHelper::getLastKey($array);

        $this->assertEquals('k3', $lastKey);
    }

    public function testGetNotNullLast()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $last = ArrayHelper::getNotNullLast($array);

        $this->assertEquals('v3', $last);
    }

    public function testGetNotNullLastOnEmpty()
    {
        $array = array(
        );

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::getNotNullLast($array);
    }

    public function testGetNotNullLastKey()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $lastKey = ArrayHelper::getNotNullLastKey($array);

        $this->assertEquals('k3', $lastKey);
    }

    public function testGetNotNullLastKeyOnEmpty()
    {
        $array = array(
        );

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::getNotNullLastKey($array);
    }

    public function testGetRange()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->assertEquals(array('k1' => 'v1', 'k2' => 'v2'),
                            ArrayHelper::getRange($array, 0, 1));

        $this->assertEquals(array('k2' => 'v2', 'k3' => 'v3'),
                            ArrayHelper::getRange($array, 1, 2));

        $this->assertEquals(array('k1' => 'v1', 'k2' => 'v2', 'k3' => 'v3'),
                            ArrayHelper::getRange($array, 0, 2));

        $this->assertEquals(array('k1' => 'v1', 'k2' => 'v2', 'k3' => 'v3'),
                            ArrayHelper::getRange($array, 0, 33));
    }

    public function testGetRangeAtFromNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array();

        ArrayHelper::getRange($array, '0', 33);
    }

    public function testGetRangeAtToNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array();

        ArrayHelper::getRange($array, 0, '33');
    }

    public function testGetRangeAtFromLessThanZero()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array();

        ArrayHelper::getRange($array, -1, 22);
    }

    public function testGetRangeAtFromEqualsZero()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array();

        ArrayHelper::getRange($array, 0, 0);
    }

    public function testGetRangeAtToLessThanFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array();

        ArrayHelper::getRange($array, 33, 25);
    }

    public function testGetListRange()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->assertEquals(array('v1', 'v2'),
                            ArrayHelper::getListRange($array, 0, 1));

        $this->assertEquals(array('v2', 'v3'),
                            ArrayHelper::getListRange($array, 1, 2));

        $this->assertEquals(array('v1', 'v2', 'v3'),
                            ArrayHelper::getListRange($array, 0, 2));

        $this->assertEquals(array('v1', 'v2', 'v3'),
                            ArrayHelper::getListRange($array, 0, 33));
    }

    public function testGetListRangeAtFromNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array();

        ArrayHelper::getListRange($array, '0', 33);
    }

    public function testGetListRangeAtToNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array();

        ArrayHelper::getListRange($array, 0, '33');
    }

    public function testGetListRangeAtFromLessThanZero()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array();

        ArrayHelper::getListRange($array, -1, 22);
    }

    public function testGetListRangeAtFromEqualsZero()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array();

        ArrayHelper::getListRange($array, 0, 0);
    }

    public function testGetListRangeAtToLessThanFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array();

        ArrayHelper::getListRange($array, 33, 25);
    }

    public function testGetRandom()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $random = ArrayHelper::getRandom($array);

        $this->assertTrue(in_array($random, $array, true));
    }

    public function testGetRandomOnEmpty()
    {
        $array = array(
        );

        $random = ArrayHelper::getRandom($array);

        $this->assertNull($random);
    }

    public function testGetRandomKey()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $randomKey = ArrayHelper::getRandomKey($array);

        $this->assertTrue(in_array($randomKey, array_keys($array), true));
    }

    public function testRemoveByKey()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        ArrayHelper::removeByKey($array, 'k3');

        $this->assertEquals(array('k1' => 'v1', 'k2' => 'v2'), $array);
    }

    public function testRemoveNotNullByKey()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        ArrayHelper::removeNotNullByKey($array, 'k3');

        $this->assertEquals(array('k1' => 'v1', 'k2' => 'v2'), $array);
    }

    public function testRemoveNotNullByKeyException()
    {
        $array = array(
        );

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeNotNullByKey($array, 'k3');
    }

    public function testRemoveNotNullByKeyFromList()
    {
        $array = array(
            'v1',
            'v2',
            'v3'
        );

        ArrayHelper::removeNotNullByKeyFromList($array, 1);

        $this->assertEquals(array('v1', 'v3'), $array);

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeNotNullByKeyFromList($array, 'key');
    }

    public function testRemoveAt()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        ArrayHelper::removeAt($array, 2);
        ArrayHelper::removeAt($array, 1);
        ArrayHelper::removeAt($array, 0);

        ArrayHelper::removeAt($array, -1);
        ArrayHelper::removeAt($array, 25);

        $this->assertTrue(empty($array));
    }

    public function testRemoveNotNullAt()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        ArrayHelper::removeNotNullAt($array, 2);
        ArrayHelper::removeNotNullAt($array, 1);
        ArrayHelper::removeNotNullAt($array, 0);

        $this->assertTrue(empty($array));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeNotNullAt($array, 25);
    }

    public function testRemoveNotNullFromListAt()
    {
        $array = array(
            'v1',
            'v2',
            'v3'
        );

        ArrayHelper::removeNotNullFromListAt($array, 1);

        $this->assertEquals(array('v1', 'v3'), $array);

        ArrayHelper::removeNotNullFromListAt($array, 1);
        ArrayHelper::removeNotNullFromListAt($array, 0);

        $this->assertTrue(empty($array));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeNotNullFromListAt($array, 25);
    }

    public function testRemoveFirst()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $v1 = ArrayHelper::removeFirst($array);

        $this->assertEquals('v1', $v1);
        $this->assertEquals(array('k2' => 'v2', 'k3' => 'v3'), $array);
    }

    public function testRemoveFirstFromList()
    {
        $array = array(
            'v1',
            'v2',
            'v3'
        );

        $v1 = ArrayHelper::removeFirst($array);

        $this->assertEquals('v1', $v1);
        $this->assertEquals(array(0 => 'v2', 1 => 'v3'), $array);
    }

    public function testRemoveNotNullFirst()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $v1 = ArrayHelper::removeNotNullFirst($array);

        $this->assertEquals('v1', $v1);
        $this->assertEquals(array('k2' => 'v2', 'k3' => 'v3'), $array);
    }

    public function testRemoveNotNullFirstOnEmpty()
    {
        $array = array(
        );

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeNotNullFirst($array);
    }

    public function testRemoveLast()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $v3 = ArrayHelper::removeLast($array);

        $this->assertEquals('v3', $v3);
        $this->assertEquals(array('k1' => 'v1', 'k2' => 'v2'), $array);
    }

    public function testRemoveLastFromList()
    {
        $array = array(
            'v1',
            'v2',
            'v3'
        );

        $v3 = ArrayHelper::removeLast($array);

        $this->assertEquals('v3', $v3);
        $this->assertEquals(array(0 => 'v1', 1 => 'v2'), $array);
    }

    public function testRemoveNotNullLast()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $v3 = ArrayHelper::removeNotNullLast($array);

        $this->assertEquals('v3', $v3);
        $this->assertEquals(array('k1' => 'v1', 'k2' => 'v2'), $array);
    }

    public function testRemoveNotNullLastOnEmpty()
    {
        $array = array(
        );

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeNotNullLast($array);
    }

    public function testRemoveValue()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v1',
            'k3' => 'v1'
        );

        ArrayHelper::removeValue($array, 'v1');

        $this->assertTrue(empty($array));

        ArrayHelper::removeValue($array, 'v1');

        $this->assertTrue(empty($array));
    }

    public function testRemoveFirstValue()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v1',
            'k3' => 'v1'
        );

        ArrayHelper::removeFirstValue($array, 'v1');

        $this->assertTrue(empty($array['k1']));
        $this->assertTrue(isset($array['k2']));
        $this->assertTrue(isset($array['k3']));

        ArrayHelper::removeFirstValue($array, 'v1');
        ArrayHelper::removeFirstValue($array, 'v1');

        $this->assertTrue(empty($array));

        ArrayHelper::removeFirstValue($array, 'v1');

        $this->assertTrue(empty($array));
    }

    public function testRemoveObject()
    {
        $object = new stdClass();

        $array = array(
            'k1' => $object,
            'k2' => $object,
            'k3' => $object
        );

        ArrayHelper::removeObject($array, $object, 'stdClass');

        $this->assertTrue(empty($array));

        ArrayHelper::removeObject($array, $object, 'stdClass');

        $this->assertTrue(empty($array));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeObject($array, $object, 'Exception');
    }

    public function testRemoveFirstObject()
    {
        $object = new stdClass();

        $array = array(
            'k1' => $object,
            'k2' => $object,
            'k3' => $object
        );

        ArrayHelper::removeFirstObject($array, $object, 'stdClass');

        $this->assertTrue(empty($array['k1']));
        $this->assertTrue(isset($array['k2']));
        $this->assertTrue(isset($array['k3']));

        ArrayHelper::removeFirstObject($array, $object, 'stdClass');
        ArrayHelper::removeFirstObject($array, $object, 'stdClass');

        $this->assertTrue(empty($array));

        ArrayHelper::removeFirstObject($array, $object, 'stdClass');

        $this->assertTrue(empty($array));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeFirstObject($array, $object, 'Exception');
    }

    public function testRemoveExistingValue()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v1',
            'k3' => 'v1'
        );

        ArrayHelper::removeExistingValue($array, 'v1');

        $this->assertTrue(empty($array));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeExistingValue($array, 'v1');
    }

    public function testRemoveExistingValueFromList()
    {
        $array = array(
            'v1',
            'v2',
            'v3'
        );

        ArrayHelper::removeExistingValueFromList($array, 'v2');

        $this->assertEquals(array('v1', 'v3'), $array);

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeExistingValueFromList($array, 'v2');
    }

    public function testRemoveExistingObject()
    {
        $object1 = new stdClass();
        $object2 = new stdClass();

        $array = array(
            'k1' => $object1,
            'k2' => $object1,
            'k3' => $object1
        );

        ArrayHelper::removeExistingObject($array, $object1, 'stdClass');

        $this->assertTrue(empty($array));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeExistingObject($array, $object2, 'stdClass');
    }

    public function testRemoveExisingObjectAtTypeError()
    {
        $this->setExpectedException('UnexpectedValueException');

        $object1 = new stdClass();

        $array = array(
            'k1' => $object1
        );

        ArrayHelper::removeExistingObject($array, $object1, 'Exception');
    }

    public function testRemoveExistingObjectFromList()
    {
        $object1 = new stdClass();
        $object2 = new stdClass();
        $object3 = new stdClass();

        $array = array(
            $object1,
            $object2,
            $object3
        );

        ArrayHelper::removeExistingObjectFromList($array, $object1, 'stdClass');

        $this->assertEquals(array($object2, $object3), $array);

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeExistingObjectFromList($array, $object1, 'stdClass');
    }

    public function testRemoveExisingObjectFromListAtTypeError()
    {
        $this->setExpectedException('UnexpectedValueException');

        $object1 = new stdClass();

        $array = array(
            $object1
        );

        ArrayHelper::removeExistingObjectFromList($array, $object1, 'Exception');
    }

    public function testRemoveFirstExistingValue()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v1',
            'k3' => 'v1'
        );

        ArrayHelper::removeFirstExistingValue($array, 'v1');
        ArrayHelper::removeFirstExistingValue($array, 'v1');
        ArrayHelper::removeFirstExistingValue($array, 'v1');

        $this->assertTrue(empty($array));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeFirstExistingValue($array, 'v1');
    }

    public function testRemoveFirstExistingObject()
    {
        $object1 = new stdClass();
        $object2 = new stdClass();

        $array = array(
            'k1' => $object1,
            'k2' => $object1,
            'k3' => $object1
        );

        ArrayHelper::removeFirstExistingObject($array, $object1, 'stdClass');
        ArrayHelper::removeFirstExistingObject($array, $object1, 'stdClass');
        ArrayHelper::removeFirstExistingObject($array, $object1, 'stdClass');

        $this->assertTrue(empty($array));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::removeFirstExistingObject($array, $object1, 'stdClass');
    }

    public function testRemoveFirstExistingObjectAtErrorType()
    {
        $this->setExpectedException('UnexpectedValueException');

        $object = new stdClass();

        $array = array(
            'k1' => $object,
        );

        ArrayHelper::removeFirstExistingObject($array, $object, 'Exception');
    }

    public function testRemoveArray()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $toRemove = array(
            'k1' => 'v1',
            'k2' => 'v2'
        );

        ArrayHelper::removeArray($array, $toRemove);

        $this->assertTrue(empty($array['k1']));
        $this->assertTrue(empty($array['k2']));
        $this->assertEquals('v3', $array['k3']);
    }

    public function testRemoveArrayNext()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $toRemove = array(
            'k1' => 'v3',
            'k2' => 'v2',
            'k3' => 'v1'
        );

        ArrayHelper::removeArray($array, $toRemove);

        $this->assertEquals('v1', $array['k1']);
        $this->assertTrue(empty($array['k2']));
        $this->assertEquals('v3', $array['k3']);
    }

    public function testRemoveArrayValues()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $toRemove = array(
            'k1' => 'v3',
            'k2' => 'v2',
            'k3' => 'v1',
            'k4' => 'v4'
        );

        ArrayHelper::removeArrayValues($array, $toRemove);

        $this->assertTrue(empty($array));
    }

    public function testRemoveArrayKeys()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $toRemove = array(
            'k1' => 'v3',
            'k2' => 'v2',
            'k3' => 'v1',
            'k4' => 'v4'
        );

        ArrayHelper::removeArrayKeys($array, $toRemove);

        $this->assertTrue(empty($array));
    }

    public function testRemoveKeysList()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $toRemove = array(
            'k1',
            'k2',
            'k3',
            'k4'
        );

        ArrayHelper::removeKeysList($array, $toRemove);

        $this->assertTrue(empty($array));
    }

    public function testRemoveRange()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        ArrayHelper::removeRange($array, 0, 2);

        $this->assertTrue(empty($array));
    }

    public function testRemoveRangeAtFromNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
        );

        ArrayHelper::removeRange($array, '0', 2);
    }

    public function testRemoveRangeAtToNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
        );

        ArrayHelper::removeRange($array, 0, '2');
    }

    public function testRemoveRangeAtFromNegative()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
        );

        ArrayHelper::removeRange($array, -1, 2);
    }

    public function testRemoveRangeAtToLessThanOrEqualsFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
        );

        ArrayHelper::removeRange($array, 2, 0);
    }

    public function testRemoveListRange()
    {
        $array = array(
            'v1',
            'v2',
            'v3'
        );

        ArrayHelper::removeListRange($array, 0, 2);

        $this->assertTrue(empty($array));
    }

    public function testRemoveListRangeAtNotList()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        ArrayHelper::removeListRange($array, '0', 2);
    }

    public function testRemoveListRangeAtFromNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'v1'
        );

        ArrayHelper::removeListRange($array, '0', 2);
    }

    public function testRemoveListRangeAtToNotInt()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'v1'
        );

        ArrayHelper::removeListRange($array, 0, '2');
    }

    public function testRemoveListRangeAtFromNegative()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'v1'
        );

        ArrayHelper::removeListRange($array, -1, 2);
    }

    public function testRemoveListRangeAtToLessThanOrEqualsFrom()
    {
        $this->setExpectedException('UnexpectedValueException');

        $array = array(
            'v1'
        );

        ArrayHelper::removeListRange($array, 2, 0);
    }

    public function testContainsKey()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->assertTrue(ArrayHelper::containsKey($array, 'k1'));
        $this->assertFalse(ArrayHelper::containsKey($array, 'k25'));
    }

    public function testContainsValue()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->assertTrue(ArrayHelper::containsValue($array, 'v1'));
        $this->assertFalse(ArrayHelper::containsValue($array, 'v25'));
    }

    public function testContainsObject()
    {
        $object = new stdClass();
        $array  = array(
            'k1' => $object
        );

        $this->assertTrue(ArrayHelper::containsObject($array, $object,
                                                      'stdClass'));
        $this->assertFalse(ArrayHelper::containsObject($array, new stdClass(),
                                                       'stdClass'));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::containsObject($array, $object, 'Exception');
    }

    public function testShuffleAsList()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        ArrayHelper::shuffleAsList($array);

        $this->assertTrue(in_array('v1', $array, true));
    }

    public function testRemoveDuplicates()
    {
        $array = array(
            'v1',
            'v2',
            'v1'
        );

        ArrayHelper::removeDuplicates($array);

        $this->assertEquals(array('v1', 'v2'), $array);
    }

    public function testReverse()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        ArrayHelper::reverse($array);

        $this->assertEquals('v3', ArrayHelper::getFirst($array));
        $this->assertEquals('k3', ArrayHelper::getFirstKey($array));
    }

    public function testFilter()
    {
        $array = array(
            22 => 'v1',
            33 => 'v2',
            null,
            0,
            ''
        );

        ArrayHelper::filter($array);

        $this->assertEquals(array(22 => 'v1', 33 => 'v2'), $array);
    }

    public function testFilterBy()
    {
        $array = array(
            'v1',
            'v2',
            null,
            0,
            ''
        );

        ArrayHelper::filterBy(
                $array,
                function($value) {
                    return !empty($value);
                }
        );

        $this->assertEquals(array('v1', 'v2'), $array);
    }

    public function testFilterByException()
    {
        $array = array(
            'v1',
            'v2',
            null,
            0,
            ''
        );

        $this->setExpectedException('InvalidArgumentException');

        ArrayHelper::filterBy($array, 'abracadabra');
    }

    public function testFilterAsList()
    {
        $array = array(
            25 => 'v1',
            31 => 'v2',
            45 => null,
            22 => 0,
            1  => ''
        );

        ArrayHelper::filterAsList($array);

        $this->assertEquals(array('v1', 'v2'), $array);
    }

    public function testFilterAsListBy()
    {
        $array = array(
            25 => 'v1',
            31 => 'v2',
            45 => null,
            22 => 0,
            1  => ''
        );

        ArrayHelper::filterAsListBy(
                $array,
                function($value) {
                    return !empty($value);
                }
        );

        $this->assertEquals(array('v1', 'v2'), $array);

        $this->setExpectedException('InvalidArgumentException');

        ArrayHelper::filterAsListBy($array, 'abraCadabraFilterFunction');
    }

    public function testReindexAsList()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        ArrayHelper::reindexAsList($array);

        $this->assertEquals('v1', $array[0]);
        $this->assertEquals('v2', $array[1]);
        $this->assertEquals('v3', $array[2]);
    }

    public function testKeyOf()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->assertEquals('k3', ArrayHelper::keyOf($array, 'v3'));
        $this->assertNull(ArrayHelper::keyOf($array, 'v4'));
    }

    public function testNotNullKeyOf()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->assertEquals('k3', ArrayHelper::notNullKeyOf($array, 'v3'));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::notNullKeyOf($array, 'v4');
    }

    public function testLastKeyOf()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v2'
        );

        $this->assertEquals('k1', ArrayHelper::lastKeyOf($array, 'v1'));
        $this->assertEquals('k3', ArrayHelper::lastKeyOf($array, 'v2'));
        $this->assertNull(ArrayHelper::lastKeyOf($array, 'v4'));
    }

    public function testNotNullLastKeyOf()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v2'
        );

        $this->assertEquals('k1', ArrayHelper::notNullLastKeyOf($array, 'v1'));
        $this->assertEquals('k3', ArrayHelper::notNullLastKeyOf($array, 'v2'));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::notNullLastKeyOf($array, 'v4');
    }

    public function testKeyOfObject()
    {
        $object1 = new stdClass();
        $object2 = new stdClass();
        $object3 = new stdClass();
        $object4 = new stdClass();

        $array = array(
            'k1' => $object1,
            'k2' => $object2,
            'k3' => $object3
        );

        $this->assertEquals('k3',
                            ArrayHelper::keyOfObject($array, $object3,
                                                     'stdClass'));
        $this->assertNull(ArrayHelper::keyOfObject($array, $object4, 'stdClass'));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::keyOfObject($array, $object4, 'Exception');
    }

    public function testNotNullKeyOfObject()
    {
        $object1 = new stdClass();
        $object2 = new stdClass();
        $object3 = new stdClass();
        $object4 = new stdClass();

        $array = array(
            'k1' => $object1,
            'k2' => $object2,
            'k3' => $object3
        );

        $this->assertEquals('k3',
                            ArrayHelper::notNullKeyOfObject($array, $object3,
                                                            'stdClass'));
        $this->assertEquals('k2',
                            ArrayHelper::notNullKeyOfObject($array, $object2,
                                                            'stdClass'));
        $this->assertEquals('k1',
                            ArrayHelper::notNullKeyOfObject($array, $object1,
                                                            'stdClass'));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::notNullKeyOfObject($array, $object4, 'Exception');
    }

    public function testNotNullKeyOfObjectAtNullKey()
    {
        $this->setExpectedException('UnexpectedValueException');

        $object = new stdClass();

        $array = array(
        );

        ArrayHelper::notNullKeyOfObject($array, $object, 'stdClass');
    }

    public function testLastKeyOfObject()
    {
        $object1 = new stdClass();
        $object2 = new stdClass();
        $object4 = new stdClass();

        $array = array(
            'k1' => $object1,
            'k2' => $object2,
            'k3' => $object2
        );

        $this->assertEquals('k1',
                            ArrayHelper::lastKeyOfObject($array, $object1,
                                                         'stdClass'));
        $this->assertEquals('k3',
                            ArrayHelper::lastKeyOfObject($array, $object2,
                                                         'stdClass'));


        $this->assertNull(ArrayHelper::lastKeyOfObject($array, $object4,
                                                       'stdClass'));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::lastKeyOfObject($array, $object4, 'Exception');
    }

    public function testNotNullLastKeyOfObject()
    {
        $object1 = new stdClass();
        $object2 = new stdClass();
        $object4 = new stdClass();

        $array = array(
            'k1' => $object1,
            'k2' => $object2,
            'k3' => $object2
        );

        $this->assertEquals('k1',
                            ArrayHelper::notNullLastKeyOfObject($array,
                                                                $object1,
                                                                'stdClass'));
        $this->assertEquals('k3',
                            ArrayHelper::notNullLastKeyOfObject($array,
                                                                $object2,
                                                                'stdClass'));

        $this->setExpectedException('UnexpectedValueException');

        ArrayHelper::notNullLastKeyOfObject($array, $object4, 'Exception');
    }

    public function testNotNullLastKeyOfObjectAtNullKey()
    {
        $this->setExpectedException('UnexpectedValueException');

        $object = new stdClass();

        $array = array(
        );

        ArrayHelper::notNullLastKeyOfObject($array, $object, 'stdClass');
    }

    public function testgGetKeys()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v2'
        );

        $this->assertEquals(array('k1', 'k2', 'k3'),
                            ArrayHelper::getKeys($array));
    }

    public function testGetKeysOfValue()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v2'
        );

        $this->assertEquals(array('k2', 'k3'),
                            ArrayHelper::getKeysOf($array, 'v2'));
    }

    public function testGetValues()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v2'
        );

        $this->assertEquals(array('v1', 'v2', 'v2'),
                            ArrayHelper::getValues($array));
    }

    public function testSortAsListBy()
    {
        $array = array(
            'k1' => 'v3',
            'k2' => 'v2',
            'k3' => 'v1'
        );

        ArrayHelper::sortAsListBy($array, 'strcmp');

        $this->assertEquals('v1', $array[0]);
        $this->assertEquals('v2', $array[1]);
        $this->assertEquals('v3', $array[2]);
    }

    public function testSortAsListByException()
    {
        $array = array(
            'k1' => 'v3',
            'k2' => 'v2',
            'k3' => 'v1'
        );

        $this->setExpectedException('InvalidArgumentException');

        ArrayHelper::sortAsListBy($array, 'abracadabra');
    }

    public function testSortAsListByDescending()
    {
        $array = array(
            'k1' => 'v2',
            'k2' => 'v3',
            'k3' => 'v1'
        );

        ArrayHelper::sortAsListByDescending($array);

        $this->assertEquals('v3', $array[0]);
        $this->assertEquals('v2', $array[1]);
        $this->assertEquals('v1', $array[2]);
    }

    public function testSortAsListByAscending()
    {
        $array = array(
            'k1' => 'v2',
            'k2' => 'v3',
            'k3' => 'v1'
        );

        ArrayHelper::sortAsListByAscending($array);

        $this->assertEquals('v1', $array[0]);
        $this->assertEquals('v2', $array[1]);
        $this->assertEquals('v3', $array[2]);
    }

    public function testSortAsHashBy()
    {
        $array = array(
            'k1' => 'v2',
            'k2' => 'v3',
            'k3' => 'v1'
        );

        ArrayHelper::sortAsHashBy($array, 'strcmp');

        $this->assertEquals('v2', $array['k1']);
        $this->assertEquals('v3', $array['k2']);
        $this->assertEquals('v1', $array['k3']);

        $this->assertEquals('v1', ArrayHelper::getFirst($array));
        $this->assertEquals('v3', ArrayHelper::getLast($array));
    }

    public function testSortAsHashByException()
    {
        $array = array(
            'k1' => 'v2',
            'k2' => 'v3',
            'k3' => 'v1'
        );

        $this->setExpectedException('InvalidArgumentException');

        ArrayHelper::sortAsHashBy($array, 'abracadabra');
    }

    public function testSortAsHashByDescending()
    {
        $array = array(
            'k1' => 'v2',
            'k2' => 'v3',
            'k3' => 'v1'
        );

        ArrayHelper::sortAsHashByDescending($array);

        $this->assertEquals('v2', $array['k1']);
        $this->assertEquals('v3', $array['k2']);
        $this->assertEquals('v1', $array['k3']);

        $this->assertEquals('v3', ArrayHelper::getFirst($array));
        $this->assertEquals('v1', ArrayHelper::getLast($array));
    }

    public function testSortAsHashByAscending()
    {
        $array = array(
            'k1' => 'v2',
            'k2' => 'v3',
            'k3' => 'v1'
        );

        ArrayHelper::sortAsHashByAscending($array);

        $this->assertEquals('v2', $array['k1']);
        $this->assertEquals('v3', $array['k2']);
        $this->assertEquals('v1', $array['k3']);

        $this->assertEquals('v1', ArrayHelper::getFirst($array));
        $this->assertEquals('v3', ArrayHelper::getLast($array));
    }

    public function testSortAsHashByNaturalOrder()
    {
        $array = array(
            'k1' => 'img12.png',
            'k2' => 'img10.png',
            'k3' => 'img2.png',
            'k4' => 'img1.png'
        );

        ArrayHelper::sortAsHashByNaturalOrder($array);

        $this->assertEquals('img12.png', $array['k1']);
        $this->assertEquals('img10.png', $array['k2']);
        $this->assertEquals('img2.png', $array['k3']);
        $this->assertEquals('img1.png', $array['k4']);

        $this->assertEquals('img1.png', ArrayHelper::getFirst($array));
        $this->assertEquals('img12.png', ArrayHelper::getLast($array));
    }

    public function testSortAsHashByNaturalOrderCaseInsensative()
    {
        $array = array(
            'k1' => 'IMG0.png',
            'k2' => 'img12.png',
            'k3' => 'img10.png',
            'k4' => 'img2.png',
            'k5' => 'img1.png',
            'k6' => 'IMG3.png'
        );

        ArrayHelper::sortAsHashByNaturalOrderCaseInsensative($array);

        $this->assertEquals('IMG0.png', $array['k1']);
        $this->assertEquals('img12.png', $array['k2']);
        $this->assertEquals('img10.png', $array['k3']);
        $this->assertEquals('img2.png', $array['k4']);
        $this->assertEquals('img1.png', $array['k5']);
        $this->assertEquals('IMG3.png', $array['k6']);

        $this->assertEquals('IMG0.png', ArrayHelper::getFirst($array));
        $this->assertEquals('img12.png', ArrayHelper::getLast($array));
    }

    public function testSortAsHashKeysBy()
    {
        $array = array(
            'k3' => 'v1',
            'k1' => 'v2',
            'k2' => 'v3'
        );

        ArrayHelper::sortAsHashKeysBy($array, 'strcmp');

        $this->assertEquals('v2', $array['k1']);
        $this->assertEquals('v3', $array['k2']);
        $this->assertEquals('v1', $array['k3']);

        $this->assertEquals('k1', ArrayHelper::getFirstKey($array));
        $this->assertEquals('k3', ArrayHelper::getLastKey($array));
    }

    public function testSortAsHashKeysByException()
    {
        $array = array(
            'k3' => 'v1',
            'k1' => 'v2',
            'k2' => 'v3'
        );

        $this->setExpectedException('InvalidArgumentException');

        ArrayHelper::sortAsHashKeysBy($array, 'abracadabra');
    }

    public function testSortAsHashKeysByDescending()
    {
        $array = array(
            'k3' => 'v1',
            'k1' => 'v2',
            'k2' => 'v3'
        );

        ArrayHelper::sortAsHashKeysByDescending($array);

        $this->assertEquals('v2', $array['k1']);
        $this->assertEquals('v3', $array['k2']);
        $this->assertEquals('v1', $array['k3']);

        $this->assertEquals('k3', ArrayHelper::getFirstKey($array));
        $this->assertEquals('k1', ArrayHelper::getLastKey($array));
    }

    public function testSortAsHashKeysByAscending()
    {
        $array = array(
            'k3' => 'v1',
            'k1' => 'v2',
            'k2' => 'v3'
        );

        ArrayHelper::sortAsHashKeysByAscending($array);

        $this->assertEquals('v2', $array['k1']);
        $this->assertEquals('v3', $array['k2']);
        $this->assertEquals('v1', $array['k3']);

        $this->assertEquals('k1', ArrayHelper::getFirstKey($array));
        $this->assertEquals('k3', ArrayHelper::getLastKey($array));
    }

    public function testImplode()
    {
        $array = array(
            'k3' => 'v1',
            'k1' => 'v2',
            'k2' => 'v3'
        );

        $this->assertEquals('v1, v2, v3', ArrayHelper::implode($array));
        $this->assertEquals('v1-v2-v3', ArrayHelper::implode($array, '-'));
    }

    public function testImplodeKeys()
    {
        $array = array(
            'k3' => 'v1',
            'k1' => 'v2',
            'k2' => 'v3'
        );

        $this->assertEquals('k3, k1, k2', ArrayHelper::implodeKeys($array));
        $this->assertEquals('k3-k1-k2', ArrayHelper::implodeKeys($array, '-'));
    }

    public function testEach()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5'
        );

        $i       = 1;
        $me      = $this;
        $handler = function(&$value, $key) use ($me, &$i) {
                    switch ($i) {
                        case 1:
                            $this->assertEquals('v1', $value);
                            $this->assertEquals('k1', $key);

                            $value = 'v11';
                            break;
                        case 2:
                            $this->assertEquals('v2', $value);
                            $this->assertEquals('k2', $key);

                            $value = 'v22';
                            break;
                        case 3:
                            $this->assertEquals('v3', $value);
                            $this->assertEquals('k3', $key);

                            $value = 'v33';
                            break;
                        case 4:
                            $this->assertEquals('v4', $value);
                            $this->assertEquals('k4', $key);

                            return false;
                            break;
                    }
                    ++$i;
                };

        ArrayHelper::each($array, $handler);

        $this->assertEquals('v11', $array['k1']);
        $this->assertEquals('v22', $array['k2']);
        $this->assertEquals('v33', $array['k3']);
        $this->assertEquals('v4', $array['k4']);
        $this->assertEquals('v5', $array['k5']);
        $this->assertEquals(4, $i);

        $i             = 0;
        $simpleHandler = function ($value, $key) use(&$i) {
                    ++$i;
                };

        $result = ArrayHelper::each($array, $simpleHandler);

        $this->assertTrue($result);
        $this->assertEquals(5, $i);

        $this->setExpectedException('InvalidArgumentException');

        ArrayHelper::each($array, 'abracadabra');
    }

    public function testEvery()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3',
            'k4' => 'v4',
            'k5' => 'v5'
        );

        $i       = 1;
        $me      = $this;
        $handler = function(&$value, $key) use ($me, &$i) {
                    switch ($i) {
                        case 1:
                            $this->assertEquals('v1', $value);
                            $this->assertEquals('k1', $key);

                            $value = 'v11';
                            break;
                        case 2:
                            $this->assertEquals('v2', $value);
                            $this->assertEquals('k2', $key);

                            $value = 'v22';
                            break;
                        case 3:
                            $this->assertEquals('v3', $value);
                            $this->assertEquals('k3', $key);

                            $value = 'v33';
                            break;
                        case 4:
                            $this->assertEquals('v4', $value);
                            $this->assertEquals('k4', $key);

                            $value = 'v44';
                            break;
                        case 5:
                            $this->assertEquals('v5', $value);
                            $this->assertEquals('k5', $key);

                            $value = 'v55';
                            break;
                    }
                    ++$i;
                };

        ArrayHelper::every($array, $handler);

        $this->assertEquals('v11', $array['k1']);
        $this->assertEquals('v22', $array['k2']);
        $this->assertEquals('v33', $array['k3']);
        $this->assertEquals('v44', $array['k4']);
        $this->assertEquals('v55', $array['k5']);
        $this->assertEquals(6, $i);

        $this->setExpectedException('InvalidArgumentException');

        ArrayHelper::every($array, 'abracadabra');
    }

}