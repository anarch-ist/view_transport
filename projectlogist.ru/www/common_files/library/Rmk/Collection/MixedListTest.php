<?php
/**
 * Автотест списка общего назначения.
 *
 * @category Rmk
 * @package  Rmk_Collection
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Collection;

use Rmk\Collection\MixedList,
    PHPUnit_Framework_TestCase;

class MixedListTest extends PHPUnit_Framework_TestCase
{

    /**
     * Тестовые данные.
     *
     * @var array
     */
    protected $array;

    /**
     * Список общего назначения.
     *
     * @var MixedList
     */
    protected $list;

    public function setUp()
    {
        $this->list = new MixedList();

        $this->array = array(
            'v1',
            'v2',
            'v3'
        );

        $this->list->fromArray($this->array);
    }

    public function tearDown()
    {
        $this->list->clear();
    }

    public function testFilterBy()
    {
        $this->list->filterBy(
                function($value) {
                    return $value === 'v3';
                }
        );

        $this->assertEquals('v3', $this->list->getByIndex(0));
        $this->assertTrue($this->list->contains('v3'));
        $this->assertFalse($this->list->contains('v1'));
        $this->assertFalse($this->list->contains('v2'));

        $this->list->filterBy(
                function($value) {
                    return false;
                }
        );

        $this->assertTrue($this->list->isEmpty());
    }

    public function testFilter()
    {
        $array = array(
            0,
            0.0,
            null,
            '',
            false,
            array()
        );

        $this->list
                ->fromArray($array)
                ->filter();

        $this->assertTrue($this->list->isEmpty());
    }

    public function testSortBy()
    {
        $this->list->sortBy(
                function($value1, $value2) {
                    return $value1 < $value2;
                }
        );

        $this->assertEquals('v3', $this->list->getByIndex(0));
        $this->assertEquals('v2', $this->list->getByIndex(1));
        $this->assertEquals('v1', $this->list->getByIndex(2));
    }

    public function testSortAscending()
    {
        $this->list->sortByAscending();

        $this->assertEquals('v1', $this->list->getByIndex(0));
        $this->assertEquals('v2', $this->list->getByIndex(1));
        $this->assertEquals('v3', $this->list->getByIndex(2));
    }

    public function testSortDescending()
    {
        $this->list->sortByDescending();

        $this->assertEquals('v3', $this->list->getByIndex(0));
        $this->assertEquals('v2', $this->list->getByIndex(1));
        $this->assertEquals('v1', $this->list->getByIndex(2));
    }

    public function testContainsIndex()
    {
        $this->assertTrue($this->list->containsIndex(0));
        $this->assertTrue($this->list->containsIndex(1));
        $this->assertTrue($this->list->containsIndex(2));

        $this->assertFalse($this->list->containsIndex(3));
    }

    public function testContainsRange()
    {
        $this->assertTrue($this->list->containsRange(0, 1));
        $this->assertTrue($this->list->containsRange(0, 2));
        $this->assertTrue($this->list->containsRange(1, 2));

        $this->assertFalse($this->list->containsRange(0, 3));
        $this->assertFalse($this->list->containsRange(1, 54));
    }

    public function testRemove()
    {
        $this->list->remove('v2');

        $this->assertEquals(array('v1', 'v3'), $this->list->toArray());
    }

    public function testRemoveAt()
    {
        $this->list->removeAt(1);

        $this->assertEquals(array('v1', 'v3'), $this->list->toArray());
    }

    public function testRemoveByIndex()
    {
        $this->list
                ->removeByIndex(2)
                ->removeByIndex(1)
                ->removeByIndex(0);

        $this->assertTrue($this->list->isEmpty());
    }

    public function testRemoveRange()
    {
        $this->list->removeRange(0, 2);

        $this->assertTrue($this->list->isEmpty());
    }

    public function testGetRange()
    {
        $this->assertEquals(array('v2', 'v3'),
                            $this->list->getRange(1, 2)->toArray());
        $this->assertEquals(array('v1', 'v2'),
                            $this->list->getRange(0, 1)->toArray());
        $this->assertEquals(array('v1', 'v2', 'v3'),
                            $this->list->getRange(0, 2)->toArray());
        $this->assertEquals(array('v1', 'v2', 'v3'),
                            $this->list->getRange(0, 33)->toArray());
    }

    public function testInsert()
    {
        $this->list->insert(0, 'v0');

        $this->assertEquals(array('v0', 'v1', 'v2', 'v3'),
                            $this->list->toArray());

        $this->list->insert(1, 'v0.1');

        $this->assertEquals(array('v0', 'v0.1', 'v1', 'v2', 'v3'),
                            $this->list->toArray());

        $this->list->insert(5, 'v4');

        $this->assertEquals(array('v0', 'v0.1', 'v1', 'v2', 'v3', 'v4'),
                            $this->list->toArray());
    }

    public function testAddFirst()
    {
        $this->list->addFirst('v0');

        $this->assertEquals(array('v0', 'v1', 'v2', 'v3'),
                            $this->list->toArray());
    }

    public function testAddLast()
    {
        $this->list->addLast('v4');

        $this->assertEquals(array('v1', 'v2', 'v3', 'v4'),
                            $this->list->toArray());
    }

    public function testSet()
    {
        $this->list->set(0, 'v00');

        $this->assertEquals(array('v00', 'v2', 'v3'), $this->list->toArray());
    }

    public function testIndexOf()
    {
        $this->assertEquals(0, $this->list->indexOf('v1'));
        $this->assertEquals(1, $this->list->indexOf('v2'));
        $this->assertEquals(2, $this->list->indexOf('v3'));
    }

    public function testLastIndexOf()
    {
        $this->list->set(2, 'v2');

        $this->assertEquals(0, $this->list->lastIndexOf('v1'));
        $this->assertEquals(2, $this->list->lastIndexOf('v2'));
    }

    public function testFromArray()
    {
        $array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->list->fromArray($array);

        $this->assertEquals($this->array, $this->list->toArray());
    }

}