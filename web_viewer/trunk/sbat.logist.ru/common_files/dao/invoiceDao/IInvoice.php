<?php
namespace DAO;
include_once __DIR__ . '/Data.php';

interface IInvoiceEntity
{
    function selectInvoices();

    function selectInvoiceByID($id);

    function updateInvoice(InvoiceData $newInvoice);

    function deleteInvoice(InvoiceData $Invoice);

    function addInvoice(InvoiceData $Invoice);

    function getInvoiceStatuses(\PrivilegedUser $pUser);

    function getInvoiceHistoryByID($invoiceID);
}