<?php
namespace DAO;
include_once __DIR__ . '/Data.php';

interface IInvoiceEntity
{
    /**
     * @return array of InvoiceData
     */
    function selectInvoices(); //covered
    //unnecessary

    function selectInvoiceByID($id);
    //unnecessary

    function updateInvoice(InvoiceData $newInvoice);

    function updateInvoiceStatus($userID, $invoiceNumber, $newInvoiceStatus, $datetime, $comment);
    //covered

    function deleteInvoice(InvoiceData $Invoice);
    //later

    function addInvoice(InvoiceData $Invoice);
    //later

    function getInvoiceStatuses(\PrivilegedUser $pUser);
    //unnecessary

    function getInvoicesForRouteList($routeListID); //covered

    function getInvoiceHistoryByInvoiceNumber($invoiceNumber); //covered
}